package com.codeinspire.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.codeinspire.dto.TaskCreateRequest;
import com.codeinspire.dto.TaskUpdateRequest;
import com.codeinspire.entity.Plan;
import com.codeinspire.entity.Task;
import com.codeinspire.entity.TaskExecutionLog;
import com.codeinspire.repository.PlanRepository;
import com.codeinspire.repository.TaskExecutionLogRepository;
import com.codeinspire.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final PlanRepository planRepository;
    private final TaskExecutionLogRepository taskExecutionLogRepository;
    private final PlanService planService;

    public Task createTask(Long userId, TaskCreateRequest request) {
        if (request.getPlanId() != null) {
            Plan plan = planRepository.selectById(request.getPlanId());
            if (plan == null || !plan.getUserId().equals(userId)) {
                throw new RuntimeException("规划不存在或无权访问");
            }
        }

        Task task = new Task();
        task.setUserId(userId);
        task.setPlanId(request.getPlanId());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus("pending");
        task.setPriority(request.getPriority() != null ? request.getPriority() : 1);
        task.setDueDate(request.getDueDate());
        task.setCategory(request.getCategory() != null ? request.getCategory() : "general");
        task.setEstimatedHours(request.getEstimatedHours());
        task.setSortOrder(0);

        taskRepository.insert(task);

        logTaskAction(task.getId(), userId, "create", null, "pending", "创建任务");

        if (request.getPlanId() != null) {
            planService.updatePlanProgress(request.getPlanId());
        }

        log.info("用户 {} 创建新任务: {}", userId, task.getTitle());
        return task;
    }

    public List<Task> getTasksByPlan(Long planId, Long userId) {
        Plan plan = planRepository.selectById(planId);
        if (plan == null || !plan.getUserId().equals(userId)) {
            throw new RuntimeException("规划不存在或无权访问");
        }

        return taskRepository.selectList(
                new LambdaQueryWrapper<Task>()
                        .eq(Task::getPlanId, planId)
                        .orderByAsc(Task::getSortOrder)
                        .orderByDesc(Task::getPriority)
        );
    }

    public List<Task> getUserTasks(Long userId, String status) {
        LambdaQueryWrapper<Task> query = new LambdaQueryWrapper<Task>()
                .eq(Task::getUserId, userId)
                .isNull(Task::getPlanId)
                .orderByDesc(Task::getCreatedAt);

        if (status != null && !status.isEmpty()) {
            query.eq(Task::getStatus, status);
        }

        return taskRepository.selectList(query);
    }

    public List<Task> getAllUserTasks(Long userId, String status) {
        LambdaQueryWrapper<Task> query = new LambdaQueryWrapper<Task>()
                .eq(Task::getUserId, userId)
                .orderByDesc(Task::getCreatedAt);

        if (status != null && !status.isEmpty()) {
            query.eq(Task::getStatus, status);
        }

        return taskRepository.selectList(query);
    }

    public Task updateTask(Long taskId, Long userId, TaskUpdateRequest request) {
        Task task = getTaskById(taskId, userId);
        String previousStatus = task.getStatus();

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
        if (request.getCategory() != null) task.setCategory(request.getCategory());
        if (request.getEstimatedHours() != null) task.setEstimatedHours(request.getEstimatedHours());

        if ("completed".equals(request.getStatus()) && task.getCompletedAt() == null) {
            task.setCompletedAt(LocalDate.now());
        } else if (!"completed".equals(request.getStatus())) {
            task.setCompletedAt(null);
        }

        taskRepository.updateById(task);

        if (!previousStatus.equals(task.getStatus())) {
            logTaskAction(task.getId(), userId, "status_change", previousStatus,
                    task.getStatus(), "状态变更: " + previousStatus + " -> " + task.getStatus());
        }

        if (task.getPlanId() != null) {
            planService.updatePlanProgress(task.getPlanId());
        }

        return task;
    }

    public void deleteTask(Long taskId, Long userId) {
        Task task = getTaskById(taskId, userId);
        Long planId = task.getPlanId();

        taskRepository.deleteById(taskId);
        logTaskAction(taskId, userId, "delete", task.getStatus(), null, "删除任务");

        if (planId != null) {
            planService.updatePlanProgress(planId);
        }
    }

    public Task completeTask(Long taskId, Long userId) {
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setStatus("completed");
        return updateTask(taskId, userId, request);
    }

    private Task getTaskById(Long taskId, Long userId) {
        Task task = taskRepository.selectById(taskId);
        if (task == null || !task.getUserId().equals(userId)) {
            throw new RuntimeException("任务不存在或无权访问");
        }
        return task;
    }

    private void logTaskAction(Long taskId, Long userId, String action,
                                 String previousStatus, String newStatus, String notes) {
        try {
            TaskExecutionLog log = new TaskExecutionLog();
            log.setTaskId(taskId);
            log.setUserId(userId);
            log.setAction(action);
            log.setPreviousStatus(previousStatus);
            log.setNewStatus(newStatus);
            log.setNotes(notes);
            taskExecutionLogRepository.insert(log);
        } catch (Exception e) {
            log.error("记录任务操作日志失败: {}", e.getMessage());
        }
    }

    public List<Task> getOverdueTasks(Long userId) {
        LocalDate today = LocalDate.now();
        return taskRepository.selectList(
                new LambdaQueryWrapper<Task>()
                        .eq(Task::getUserId, userId)
                        .lt(Task::getDueDate, today)
                        .ne(Task::getStatus, "completed")
                        .ne(Task::getStatus, "cancelled")
                        .orderByAsc(Task::getDueDate)
        );
    }

    public List<Task> getTodayTasks(Long userId) {
        LocalDate today = LocalDate.now();
        return taskRepository.selectList(
                new LambdaQueryWrapper<Task>()
                        .eq(Task::getUserId, userId)
                        .le(Task::getDueDate, today)
                        .ne(Task::getStatus, "completed")
                        .ne(Task::getStatus, "cancelled)
                        .orderByAsc(Task::getSortOrder)
                        .orderByDesc(Task::getPriority)
        );
    }

    public List<Task> getUpcomingTasks(Long userId, int days) {
        LocalDate today = LocalDate.now();
        LocalDate future = today.plusDays(days);
        return taskRepository.selectList(
                new LambdaQueryWrapper<Task>()
                        .eq(Task::getUserId, userId)
                        .gt(Task::getDueDate, today)
                        .le(Task::getDueDate, future)
                        .ne(Task::getStatus, "completed)
                        .ne(Task::getStatus, "cancelled)
                        .orderByAsc(Task::getDueDate)
        );
    }
}
