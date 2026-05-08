package com.codeinspire.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmergencyService {

    private final ObjectMapper objectMapper;

    @Data
    public static class EmergencyScenario {
        private String type;
        private String triggerKeywords;
        private int severityLevel;
        private String responseTemplate;
        private List<String> suggestedActions;
        private boolean requiresHumanIntervention;
    }

    @Data
    public static class EmergencyResponse {
        private boolean isEmergency;
        private EmergencyScenario scenario;
        private String responseMessage;
        private List<String> actions;
        private String comfortMessage;
        private Map<String, Object> metadata;
    }

    private static final Map<String, EmergencyScenario> EMERGENCY_SCENARIOS = new HashMap<>();

    static {
        EMERGENCY_SCENARIOS.put("exam_failure", new EmergencyScenario() {{
            setType("exam_failure");
            setTriggerKeywords("考研失败,考砸了,没考上,落榜,成绩不好,挂科");
            setSeverityLevel(3);
            setResponseTemplate("我理解你现在可能感到失落和迷茫。考试失利并不代表你的能力有问题，它只是说明这次准备或发挥不够理想。让我们一起分析一下情况，制定下一步的计划。");
            setSuggestedActions(Arrays.asList(
                    "冷静下来，给自己1-2天时间调整情绪",
                    "分析失败原因：是知识储备不足、心态问题还是策略问题？",
                    "评估是否需要二战，或者考虑其他发展路径",
                    "关注春招/补录机会",
                    "制定短期目标，找回学习节奏"
            ));
            setRequiresHumanIntervention(false);
        }});

        EMERGENCY_SCENARIOS.put("recruitment_failure", new EmergencyScenario() {{
            setType("recruitment_failure");
            setTriggerKeywords("秋招失败,春招被拒,面试挂了,没有offer,全部被拒,找不到工作,求职受挫");
            setSeverityLevel(3);
            setResponseTemplate("求职受挫确实很打击人，但请记住：这并不是对你个人能力的否定。很多优秀的程序员都经历过类似的挫折。让我们一起来分析原因并找到突破口。");
            setSuggestedActions(Arrays.asList(
                    "复盘：整理所有面试记录，找出共性问题",
                    "针对性补强：根据面试反馈提升薄弱环节",
                    "调整预期：适当降低目标企业层级，先积累经验",
                    "拓展渠道：除了大厂，关注中厂、创业公司机会",
                    "项目补充：用1-2个月做有含金量的项目"
            ));
            setRequiresHumanIntervention(false);
        }});

        EMERGENCY_SCENARIOS.put("internship_terminated", new EmergencyScenario() {{
            setType("internship_terminated");
            setTriggerKeywords("实习被裁,实习结束,被辞退,裁员,优化,HC锁");
            setSeverityLevel(4);
            setResponseTemplate("实习被终止确实让人措手不及，但这在当前环境下并不罕见。重要的是保持积极心态，快速调整方向。");
            setSuggestedActions(Arrays.asList(
                    "不要自我否定：实习被裁往往与个人能力无关",
                    "总结经验：记录学到的技术和业务知识",
                    "更新简历：将实习经历转化为亮点",
                    "立即投递：关注其他公司实习/校招机会",
                    "技能提升：利用空档期补强技术栈"
            ));
            setRequiresHumanIntervention(false);
        }});

        EMERGENCY_SCENARIOS.put("interview_failure", new EmergencyScenario() {{
            setType("interview_failure");
            setTriggerKeywords("面试失败,面试挂了,二面挂,三面挂,HR面挂,技术面挂,算法题不会");
            setSeverityLevel(2);
            setResponseTemplate("面试失败是成长的一部分。每一次失败都是宝贵的学习机会。让我帮你分析一下可能的改进方向。");
            setSuggestedActions(Arrays.asList(
                    "复盘面试题：记录所有问题，分类整理",
                    "查漏补缺：针对薄弱知识点系统学习",
                    "模拟练习：找同学进行mock interview",
                    "调整策略：优化简历和自我介绍",
                    "保持信心：继续投递，量变引起质变"
            ));
            setRequiresHumanIntervention(false);
        }});

        EMERGENCY_SCENARIOS.put("tech_anxiety", new EmergencyScenario() {{
            setType("tech_anxiety");
            setTriggerKeywords("焦虑,担心,害怕,迷茫,不知道学什么,技术太多学不完,跟不上,掉队,内卷");
            setSeverityLevel(2);
            setResponseTemplate("技术焦虑是非常普遍的感受。计算机领域确实变化很快，但核心原理相对稳定。让我们把问题拆解成可执行的小步骤。");
            setSuggestedActions(Arrays.asList(
                    "聚焦核心：掌握一门语言+一个框架的深度",
                    "设定小目标：每周完成一个小任务建立成就感",
                    "减少比较：关注自己的进步而非他人进度",
                    "寻找社区：加入学习小组互相鼓励",
                    "实践导向：通过项目学习而非纯理论学习"
            ));
            setRequiresHumanIntervention(false);
        }});

        EMERGENCY_SCENARIOS.put("crisis_emotional", new EmergencyScenario() {{
            setType("crisis_emotional");
            setTriggerKeywords("不想活了,活不下去了,绝望,抑郁,想死,活着没意思,崩溃");
            setSeverityLevel(5);
            setResponseTemplate("我听到你现在的痛苦了。虽然我是一个AI助手，但我真的很关心你的感受。你现在的感受是真实的、合理的，但请不要独自承受这些。");
            setSuggestedActions(Arrays.asList(
                    "如果你正在经历严重的负面情绪，请联系专业帮助",
                    "全国心理援助热线：400-161-9995（24小时）",
                    "北京心理危机研究与干预中心：010-82951332",
                    "与信任的朋友、家人或老师倾诉",
                    "暂时放下学习和求职压力，优先照顾好自己的身心健康"
            ));
            setRequiresHumanIntervention(true);
        }});
    }

    public EmergencyResponse detectAndHandle(String userMessage) {
        if (userMessage == null || userMessage.isBlank()) {
            return createNonEmergencyResponse();
        }

        for (Map.Entry<String, EmergencyScenario> entry : EMERGENCY_SCENARIOS.entrySet()) {
            EmergencyScenario scenario = entry.getValue();

            String[] keywords = scenario.getTriggerKeywords().split(",");
            for (String keyword : keywords) {
                if (userMessage.contains(keyword.trim())) {
                    return createEmergencyResponse(scenario);
                }
            }
        }

        return createNonEmergencyResponse();
    }

    private EmergencyResponse createEmergencyResponse(EmergencyScenario scenario) {
        EmergencyResponse response = new EmergencyResponse();
        response.setEmergency(true);
        response.setScenario(scenario);
        response.setResponseMessage(scenario.getResponseTemplate());
        response.setActions(scenario.getSuggestedActions());

        if (scenario.getSeverityLevel() >= 4) {
            response.setComfortMessage("请记住，这只是人生中的一个小插曲。你已经很棒了，给自己一些时间和空间。");
        } else {
            response.setComfortMessage("每个人都会遇到困难，重要的是我们如何应对。你并不孤单。");
        }

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("severityLevel", scenario.getSeverityLevel());
        metadata.put("detectedAt", new Date());
        metadata.put("requiresHumanIntervention", scenario.isRequiresHumanIntervention());
        response.setMetadata(metadata);

        log.warn("检测到紧急场景: {}, 严重程度: {}", scenario.getType(), scenario.getSeverityLevel());

        return response;
    }

    private EmergencyResponse createNonEmergencyResponse() {
        EmergencyResponse response = new EmergencyResponse();
        response.setEmergency(false);
        return response;
    }

    public List<EmergencyScenario> getAllScenarios() {
        return new ArrayList<>(EMERGENCY_SCENARIOS.values());
    }

    public String getComfortingResponse(String userType) {
        String baseResponse = switch (userType) {
            case "985" -> "作为985的学生，你有很好的基础和资源。暂时的挫折不代表什么，相信自己的潜力。";
            case "211" -> "211背景已经让你领先很多人了。保持耐心，持续努力，结果会来的。";
            case "普通一本" -> "学历只是一个起点，真正决定你的是能力和态度。很多成功的技术人员来自普通学校。";
            case "二本" -> "二本背景完全可以通过项目和实力弥补。专注提升自己，用作品说话。";
            case "民办/专科" -> "不要因为学校标签而否定自己。技术行业看重的是实际能力，努力一定会有回报。";
            default -> "无论背景如何，每个人都有自己的节奏和路径。专注于自己的成长就好。";
        };

        return baseResponse + "\n\n记住：编程是一门可以通过练习不断精进的技能。今天比昨天进步一点点，就是成功。";
    }
}
