package com.codeinspire.service;

import com.codeinspire.entity.UserProfile;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeNodeReminderService {

    @Data
    public static class TimeNode {
        private String id;
        private String title;
        private String description;
        private String grade;
        private Month startMonth;
        private Month endMonth;
        private int daysBeforeReminder;
        private String category;
        private int priority;
        private List<String> suggestedActions;
    }

    @Data
    public static class ReminderResult {
        private List<TimeNode> upcomingNodes;
        private List<TimeNode> activeNodes;
        private List<TimeNode> missedNodes;
        private String currentPhase;
        private String phaseDescription;
        private int daysUntilNextImportant;
    }

    private static final List<TimeNode> TIME_NODES = Arrays.asList(
            new TimeNode() {{
                setId("freshman_end");
                setTitle("大一结束 - 实习准备开始");
                setDescription("大一期末是开始规划的好时机，可以开始了解实习信息和技术方向");
                setGrade("大一");
                setStartMonth(Month.JUNE);
                setEndMonth(Month.JULY);
                setDaysBeforeReminder(30);
                setCategory("planning");
                setPriority(2);
                setSuggestedActions(Arrays.asList(
                        "回顾大一学年的收获和不足",
                        "确定感兴趣的技术方向",
                        "开始学习基础编程技能",
                        "关注学长学姐的实习经验分享"
                ));
            }},
            new TimeNode() {{
                setId("sophomore_spring_intern");
                setTitle("大二春季 - 暑期实习申请季");
                setDescription("3-4月是大二学生申请暑期实习的关键时期");
                setGrade("大二");
                setStartMonth(Month.MARCH);
                setEndMonth(Month.APRIL);
                setDaysBeforeReminder(45);
                setCategory("recruitment");
                setPriority(1);
                setSuggestedActions(Arrays.asList(
                        "完善简历和GitHub项目",
                        "复习数据结构和算法",
                        "投递暑期实习岗位",
                        "准备技术面试"
                ));
            }},
            new TimeNode() {{
               setId("sophomore_summer_intern");
                setTitle("大二暑假 - 暑期实习关键期");
                setDescription("6-8月是暑期实习的黄金时间，认真把握每一次实践机会");
                setGrade("大二");
                setStartMonth(Month.JUNE);
                setEndMonth(Month.AUGUST);
                setDaysBeforeReminder(15);
                setCategory("practice");
                setPriority(1);
                setSuggestedActions(Arrays.asList(
                        "全身心投入实习工作",
                        "记录每天的学习和成长",
                        "建立与同事的良好关系",
                        "思考自己的职业方向"
                ));
            }},
            new TimeNode() {{
                setId("junior_autumn_review");
                setTitle("大三秋季 - 暑期实习复盘+秋招提前批");
                setDescription("9-11月：复盘暑期实习成果，同时开始关注秋招提前批机会");
                setGrade("大三");
                setStartMonth(Month.SEPTEMBER);
                setEndMonth(Month.NOVEMBER);
                setDaysBeforeReminder(30);
                setCategory("recruitment");
                setPriority(1);
                setSuggestedActions(Arrays.asList(
                        "总结暑期实习的收获",
                        "补充项目经历",
                        "关注秋招提前批信息",
                        "开始刷LeetCode高频题"
                ));
            }},
            new TimeNode() {{
                setId("junior_spring_recruit");
                setTitle("大三春季 - 春招实习+秋招备战");
                setDescription("3-5月：参加春招实习，同时为秋招做全面准备");
                setGrade("大三");
                setStartMonth(Month.MARCH);
                setEndMonth(Month.MAY);
                setDaysBeforeReminder(30);
                setCategory("recruitment");
                setPriority(1);
                setSuggestedActions(Arrays.asList(
                        "投递春招实习岗位",
                        "系统复习计算机基础",
                        "完成2-3个高质量项目",
                        "模拟面试练习"
                ));
            }},
            new TimeNode() {{
                setId("junior_summer_prep");
                setTitle("大三暑假 - 暑期实习+秋招备战");
                setDescription("6-8月：继续实习或全力备战秋招");
                setGrade("大三");
                setStartMonth(Month.JUNE);
                setEndMonth(Month.AUGUST);
                setDaysBeforeReminder(20);
                setCategory("preparation");
                setPriority(1);
                setSuggestedActions(Arrays.asList(
                        "如果无实习则全力刷题",
                        "整理简历和作品集",
                        "建立面试题库",
                        "保持良好作息"
                ));
            }},
            new TimeNode() {{
                setId("senior_autumn_recruit");
                setTitle("大四秋季 - 秋招正式批（金九银十）");
                setDescription("8-10月：秋招最关键的黄金时期！全力以赴！");
                setGrade("大四");
                setStartMonth(Month.AUGUST);
                setEndMonth(Month.OCTOBER);
                setDaysBeforeReminder(60);
                setCategory("critical");
                setPriority(0);
                setSuggestedActions(Arrays.asList(
                        "大量投递简历（目标50+）",
                        "积极参加宣讲会和双选会",
                        "每场面试后及时复盘",
                        "保持积极心态"
                ));
            }},
            new TimeNode() {{
                setId("senior_late_autumn");
                setTitle("大四深秋 - 秋招补录+春招准备");
                setDescription("11-12月：秋招收尾，开始为春招做准备");
                setGrade("大四");
                setStartMonth(Month.NOVEMBER);
                setEndMonth(Month.DECEMBER);
                setDaysBeforeReminder(30);
                setCategory("transition");
                setPriority(2);
                setSuggestedActions(Arrays.asList(
                        "跟进秋招offer流程",
                        "总结秋招经验教训",
                        "查漏补缺提升短板",
                        "准备春招策略"
                ));
            }},
            new TimeNode() {{
                setId("senior_spring_recruit");
                setTitle("大四春季 - 春招补录+毕设冲刺");
                setDescription("3-5月：春招最后机会 + 毕业设计冲刺");
                setGrade("大四");
                setStartMonth(Month.MARCH);
                setEndMonth(Month.MAY);
                setDaysBeforeReminder(30);
                setCategory("final");
                setPriority(1);
                setSuggestedActions(Arrays.asList(
                        "抓住春招最后机会",
                        "平衡求职和毕设",
                        "考虑降低预期或拓宽选择",
                        "做好两手准备"
                ));
            }}
    );

    public ReminderResult getRemindersForUser(UserProfile profile) {
        if (profile == null || profile.getGrade() == null) {
            return createDefaultReminders();
        }

        String grade = profile.getGrade();
        LocalDate today = LocalDate.now();
        Month currentMonth = today.getMonth();

        ReminderResult result = new ReminderResult();
        result.setUpcomingNodes(new ArrayList<>());
        result.setActiveNodes(new ArrayList<>());
        result.setMissedNodes(new ArrayList<>());

        for (TimeNode node : TIME_NODES) {
            if (!node.getGrade().equals(grade)) continue;

            if (isNodeActive(node, currentMonth)) {
                result.getActiveNodes().add(node);
            } else if (isNodeUpcoming(node, today)) {
                result.getUpcomingNodes().add(node);
            } else if (isNodeMissed(node, today)) {
                result.getMissedNodes().add(node);
            }
        }

        result.setActiveNodes(sortByPriority(result.getActiveNodes()));
        result.setUpcomingNodes(sortByPriority(result.getUpcomingNodes()));

        setCurrentPhase(result, grade, currentMonth);

        return result;
    }

    private boolean isNodeActive(TimeNode node, Month currentMonth) {
        if (node.getStartMonth() == null || node.getEndMonth() == null) return false;

        if (node.getStartMonth().getValue() <= node.getEndMonth().getValue()) {
            return currentMonth.getValue() >= node.getStartMonth().getValue()
                    && currentMonth.getValue() <= node.getEndMonth().getValue();
        } else {
            return currentMonth.getValue() >= node.getStartMonth().getValue()
                    || currentMonth.getValue() <= node.getEndMonth().getValue();
        }
    }

    private boolean isNodeUpcoming(TimeNode node, LocalDate today) {
        if (node.getStartMonth() == null) return false;

        LocalDate nodeStart = today.with(node.getStartMonth()).withDayOfMonth(1);
        long daysUntil = java.time.temporal.ChronoUnit.DAYS.between(today, nodeStart);

        return daysUntil > 0 && daysUntil <= node.getDaysBeforeReminder();
    }

    private boolean isNodeMissed(TimeNode node, LocalDate today) {
        if (node.getEndMonth() == null) return false;

        LocalDate nodeEnd = today.with(node.getEndMonth()).withDayOfMonth(
                node.getEndMonth().length(today.isLeapYear())
        );
        return today.isAfter(nodeEnd);
    }

    private List<TimeNode> sortByPriority(List<TimeNode> nodes) {
        nodes.sort(Comparator.comparingInt(TimeNode::getPriority));
        return nodes;
    }

    private void setCurrentPhase(ReminderResult result, String grade, Month month) {
        String phase = switch (grade) {
            case "大一" -> "基础积累期";
            case "大二" -> "探索与实践期";
            case "大三" -> "关键冲刺期";
            case "大四" -> "求职决战期";
            default -> "持续发展期";
        };

        result.setCurrentPhase(phase);
        result.setPhaseDescription(getPhaseDescription(grade, month));

        Optional<Integer> minDays = result.getActiveNodes().stream()
                .map(n -> getDaysToNodeStart(n, LocalDate.now()))
                .filter(d -> d > 0)
                .min(Integer::compareTo);

        result.setDaysUntilNextImportant(minDays.orElse(-1));
    }

    private int getDaysToNodeStart(TimeNode node, LocalDate today) {
        if (node.getStartMonth() == null) return Integer.MAX_VALUE;
        LocalDate start = today.with(node.getStartMonth()).withDayOfMonth(1);
        return (int) java.time.temporal.ChronoUnit.DAYS.between(today, start);
    }

    private String getPhaseDescription(String grade, Month month) {
        return switch (grade) {
            case "大一" -> "当前阶段重点是打好编程基础，培养学习兴趣。建议从一门语言开始深入学习。";
            case "大二" -> "当前阶段应该开始尝试实际项目，参与竞赛或开源活动，积累实践经验。";
            case "大三" -> "这是最关键的一年！需要平衡学业、项目和实习准备，为秋招打好基础。";
            case "大四" -> "求职或深造的关键决策期。根据自身情况制定明确的行动计划。";
            default -> "持续学习和成长，保持对技术的热情。";
        };
    }

    private ReminderResult createDefaultReminders() {
        ReminderResult result = new ReminderResult();
        result.setUpcomingNodes(Collections.emptyList());
        result.setActiveNodes(Collections.emptyList());
        result.setMissedNodes(Collections.emptyList());
        result.setCurrentPhase("未设置年级信息");
        result.setPhaseDescription("请先完善个人画像中的年级信息，以获取个性化的时间节点提醒。");
        result.setDaysUntilNextImportant(-1);
        return result;
    }

    public List<TimeNode> getAllTimeNodes() {
        return TIME_NODES;
    }
}
