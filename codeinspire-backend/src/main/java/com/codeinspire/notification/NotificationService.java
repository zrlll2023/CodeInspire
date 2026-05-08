package com.codeinspire.notification;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.codeinspire.entity.Notification;
import com.codeinspire.entity.NotificationSetting;
import com.codeinspire.repository.NotificationRepository;
import com.codeinspire.repository.NotificationSettingRepository;
import com.codeinspire.websocket.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationSettingRepository notificationSettingRepository;
    private final WebSocketHandler webSocketHandler;

    public Notification createNotification(Long userId, String type, String title, String content, Map<String, Object> data) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);

        if (data != null) {
            try {
                notification.setData(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(data));
            } catch (Exception e) {
                notification.setData(null);
            }
        }

        notification.setIsRead(false);
        notification.setChannel("in_app");
        notification.setSendStatus("sent");
        notification.setSentAt(LocalDateTime.now());

        notificationRepository.insert(notification);

        try {
            webSocketHandler.sendToUser(userId, Map.of(
                    "type", "notification",
                    "notificationId", notification.getId(),
                    "notificationType", type,
                    "title", title,
                    "content", content,
                    "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            log.error("WebSocket推送通知失败: {}", e.getMessage());
        }

        return notification;
    }

    public List<Notification> getUserNotifications(Long userId, Boolean isRead) {
        LambdaQueryWrapper<Notification> query = new LambdaQueryWrapper<Notification>()
                .eq(Notification::getUserId, userId)
                .orderByDesc(Notification::getCreatedAt);

        if (isRead != null) {
            query.eq(Notification::getIsRead, isRead);
        }

        return notificationRepository.selectList(query);
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.selectCount(
                new LambdaQueryWrapper<Notification>()
                        .eq(Notification::getUserId, userId)
                        .eq(Notification::getIsRead, false)
        );
    }

    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.selectById(notificationId);
        if (notification != null && notification.getUserId().equals(userId)) {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
            notificationRepository.updateById(notification);
        }
    }

    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.selectList(
                new LambdaQueryWrapper<Notification>()
                        .eq(Notification::getUserId, userId)
                        .eq(Notification::getIsRead, false)
        );

        for (Notification notification : unreadNotifications) {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
            notificationRepository.updateById(notification);
        }
    }

    public void deleteNotification(Long notificationId, Long userId) {
        Notification notification = notificationRepository.selectById(notificationId);
        if (notification != null && notification.getUserId().equals(userId)) {
            notificationRepository.deleteById(notificationId);
        }
    }

    public NotificationSetting getOrCreateSettings(Long userId) {
        NotificationSetting settings = notificationSettingRepository.selectOne(
                new LambdaQueryWrapper<NotificationSetting>().eq(NotificationSetting::getUserId, userId)
        );

        if (settings == null) {
            settings = createDefaultSettings(userId);
        }

        return settings;
    }

    public NotificationSetting updateSettings(Long userId, Map<String, Object> updates) {
        NotificationSetting settings = getOrCreateSettings(userId);

        if (updates.containsKey("taskReminderEnabled")) {
            settings.setTaskReminderEnabled((Boolean) updates.get("taskReminderEnabled"));
        }
        if (updates.containsKey("timeNodeEnabled")) {
            settings.setTimeNodeEnabled((Boolean) updates.get("timeNodeEnabled"));
        }
        if (updates.containsKey("progressWarningEnabled")) {
            settings.setProgressWarningEnabled((Boolean) updates.get("progressWarningEnabled"));
        }
        if (updates.containsKey("aiReplyEnabled")) {
            settings.setAiReplyEnabled((Boolean) updates.get("aiReplyEnabled"));
        }
        if (updates.containsKey("systemAnnouncementEnabled")) {
            settings.setSystemAnnouncementEnabled((Boolean) updates.get("systemAnnouncementEnabled"));
        }
        if (updates.containsKey("quietStartHour")) {
            settings.setQuietStartHour((String) updates.get("quietStartHour"));
        }
        if (updates.containsKey("quietEndHour")) {
            settings.setQuietEndHour((String) updates.get("quietEndHour"));
        }

        notificationSettingRepository.updateById(settings);
        return settings;
    }

    private NotificationSetting createDefaultSettings(Long userId) {
        NotificationSetting settings = new NotificationSetting();
        settings.setUserId(userId);
        settings.setTaskReminderEnabled(true);
        settings.setTimeNodeEnabled(true);
        settings.setProgressWarningEnabled(true);
        settings.setAiReplyEnabled(true);
        settings.setSystemAnnouncementEnabled(true);
        settings.setQuietStartHour("22:00");
        settings.setQuietEndHour("08:00");

        notificationSettingRepository.insert(settings);
        return settings;
    }

    public void sendTaskReminder(Long userId, String taskTitle, LocalDate dueDate) {
        NotificationSetting settings = getOrCreateSettings(userId);
        if (!Boolean.TRUE.equals(settings.getTaskReminderEnabled())) return;

        createNotification(userId, "task_reminder",
                "任务提醒",
                "任务「" + taskTitle + "」即将到期，截止日期：" + dueDate,
                Map.of("type", "task_reminder", "taskTitle", taskTitle, "dueDate", dueDate.toString()));
    }

    public void sendTimeNodeReminder(Long userId, String nodeName, String description) {
        NotificationSetting settings = getOrCreateSettings(userId);
        if (!Boolean.TRUE.equals(settings.getTimeNodeEnabled())) return;

        createNotification(userId, "time_node",
                "重要时间节点",
                nodeName + ": " + description,
                Map.of("type", "time_node", "nodeName", nodeName));
    }

    public void sendProgressWarning(Long userId, String planName, double progress) {
        NotificationSetting settings = getOrCreateSettings(userId);
        if (!Boolean.TRUE.equals(settings.getProgressWarningEnabled())) return;

        createNotification(userId, "progress_warning",
                "进度预警",
                "规划「" + planName + "」进度落后，当前完成度：" + Math.round(progress * 100) + "%",
                Map.of("type", "progress_warning", "planName", planName, "progress", progress));
    }

    public void sendAiReplyNotification(Long userId, String messagePreview) {
        NotificationSetting settings = getOrCreateSettings(userId);
        if (!Boolean.TRUE.equals(settings.getAiReplyEnabled())) return;

        createNotification(userId, "ai_reply",
                "AI回复",
                "AI顾问已回复您的消息: " + (messagePreview.length() > 50 ? messagePreview.substring(0, 50) + "..." : messagePreview),
                Map.of("type", "ai_reply", "preview", messagePreview));
    }
}
