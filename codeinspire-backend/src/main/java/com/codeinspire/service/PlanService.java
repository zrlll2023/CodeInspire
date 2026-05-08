package com.codeinspire.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.codeinspire.dto.PlanCreateRequest;
import com.codeinspire.entity.Plan;
import com.codeinspire.entity.Task;
import com.codeinspire.repository.PlanRepository;
import com.codeinspire.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final TaskRepository taskRepository;

    public Plan createPlan(Long userId, PlanCreateRequest request) {
        Plan plan = new Plan();
        plan.setUserId(userId);
        plan.setTitle(request.getTitle());
        plan.setDescription(request.getDescription());
        plan.setType(request.getType() != null ? request.getType() : "learning");
        plan.setStatus("active");
        plan.setStartDate(request.getStartDate());
        plan.setEndDate(request.getEndDate());
        plan.setTargetGoal(request.getTargetGoal());
        plan.setPriority(request.getPriority() != null ? request.getPriority() : 1);
        plan.setTotalTasks(0);
        plan.setCompletedTasks(0);

        planRepository.insert(plan);
        log.info("用户 {} 创建新规划: {}", userId, plan.getTitle());
        return plan;
    }

    public List<Plan> getUserPlans(Long userId, String status) {
        LambdaQueryWrapper<Plan> query = new LambdaQueryWrapper<Plan>()
                .eq(Plan::getUserId, userId)
                .orderByDesc(Plan::getCreatedAt);

        if (status != null && !status.isEmpty()) {
            query.eq(Plan::getStatus, status);
        }

        return planRepository.selectList(query);
    }

    public Plan getPlanById(Long planId, Long userId) {
        Plan plan = planRepository.selectById(planId);
        if (plan == null || !plan.getUserId().equals(userId)) {
            throw new RuntimeException("规划不存在或无权访问");
        }
        return plan;
    }

    public Plan updatePlan(Long planId, Long userId, PlanCreateRequest request) {
        Plan plan = getPlanById(planId, userId);

        if (request.getTitle() != null) plan.setTitle(request.getTitle());
        if (request.getDescription() != null) plan.setDescription(request.getDescription());
        if (request.getType() != null) plan.setType(request.getType());
        if (request.getStartDate() != null) plan.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) plan.setEndDate(request.getEndDate());
        if (request.getTargetGoal() != null) plan.setTargetGoal(request.getTargetGoal());
        if (request.getPriority() != null) plan.setPriority(request.getPriority());

        planRepository.updateById(plan);
        return plan;
    }

    public void deletePlan(Long planId, Long userId) {
        Plan plan = getPlanById(planId, userId);
        plan.setStatus("archived");
        planRepository.updateById(plan);
    }

    public void completePlan(Long planId, Long userId) {
        Plan plan = getPlanById(planId, userId);
        plan.setStatus("completed");

        List<Task> tasks = taskRepository.selectList(
                new LambdaQueryWrapper<Task>().eq(Task::getPlanId, planId)
        );
        long completedCount = tasks.stream()
                .filter(t -> "completed".equals(t.getStatus()))
                .count();

        plan.setCompletedTasks((int) completedCount);
        plan.setTotalTasks(tasks.size());

        planRepository.updateById(plan);
        log.info("规划 {} 已完成", plan.getTitle());
    }

    public void updatePlanProgress(Long planId) {
        Plan plan = planRepository.selectById(planId);
        if (plan == null) return;

        List<Task> tasks = taskRepository.selectList(
                new LambdaQueryWrapper<Task>().eq(Task::getPlanId, planId)
        );

        long total = tasks.size();
        long completed = tasks.stream().filter(t -> "completed".equals(t.getStatus())).count();

        plan.setTotalTasks((int) total);
        plan.setCompletedTasks((int) completed);

        if (total > 0 && completed == total) {
            plan.setStatus("completed");
        } else if ("completed".equals(plan.getStatus())) {
            plan.setStatus("active");
        }

        planRepository.updateById(plan);
    }
}
