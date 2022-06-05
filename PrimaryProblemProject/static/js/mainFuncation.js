$(function () {
    // 获取临时用户标识
    getTempToken();
    // 获取atomToken
    getAtomToken();

    $("#generateBtn").click(function () {
        generatePaper();
    })
    // generatePaper();
    $("#submitPaper").click(function () {
        submitPaper();
    });

    // console.log($("#score").text() == "")

})

function getAtomToken() {

    $.ajax({
        url: "http://118.31.113.63:8080/getAtomToken",
        type: "get",
        success: function (token) {
            $("#atomToken").val(token);
        }
    });
}

function submitPaper() {

    var problemAnswerVo = new Object();
    problemAnswerVo.token = $("#paperToken").val();
    var answers = [];

    var inputs = $("input.problem");
    $.each(inputs, function (index, item) {
        var answer = new Object();
        answer.answerBody = $(item).val();
        answer.id = $(item).parent().attr("id");
        answers.push(answer);
    });
    problemAnswerVo.answers = answers;

    console.log("answers：" + answers);
    $.ajax({
        url: "http://118.31.113.63:8080/correctProblem",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify(problemAnswerVo),
        success: function (result) {
            console.log(result);
            if (result.code == 4002) {
                layer.msg(result.msg);
                return false;
            }
            // return  false;
            renderScore(result.scoreInfoVo);
        }
    });
}

function renderScore(scoreInfoVo) {
    // 显示总分
    var scoreSpan = $("<span id='score' style=\"padding-left: 200px ;font-size: 20px;color: red\"></span>").text("总分：" + scoreInfoVo.score);
    $("#headTitleInfo").append(scoreSpan);

    $.each(scoreInfoVo.problemAnswers, function (index, answer) {
        var resultInfo = $("<span style='color: red;padding-left: 30px;'></span>").text(answer.answerBody);
        $("#modalBody p[id= " + answer.id + "]").append(resultInfo);
    })

    $("#atomToken").val(scoreInfoVo.atomToken);
}

function getTempToken() {
    $.ajax({
        url: "http://118.31.113.63:8080/getTempToken",
        type: "get",
        success: function (token) {
            $("#tempToken").val(token);
        }
    });
}

function renderPaper(problemVo) {
    console.log(problemVo);
    $("#paperToken").val(problemVo.token);
    // $("#atomToken").val(problemVo.atomToken);
    $.each(problemVo.problems, function (index, item) {
        var p = $("<p style='padding-left: 100px'></p>").text(item.problemBody + ' = ').attr("id", item.id);
        $("<input style='width: 100px' class='problem' type='text'></input>").appendTo(p);
        $("#modalBody").append(p);
    })
    $("#testPaper").css("display", "block");
}

function generatePaper() {

    // 清空上次的内容
    // flushContentBefore();
    // 清空上次现实的分数信息
    // $("#score").text("");

    var problemInfo = new Object();
    problemInfo.calStyle = [];
    $("input[name=calStyle]:checked").each(function () {
        problemInfo.calStyle.push($(this).val());
    })
    // problemInfo.calStyle = $("input[name=calStyle]:checked").val();
    problemInfo.maxNum = $("input[name=maxNum]").val();
    problemInfo.stepNum = $("input[name=stepNum]").val();
    problemInfo.problemNum = $("input[name=problemNum]").val();
    problemInfo.token = $("#tempToken").val();
    // problemInfo.atomToken = $("#atomToken").val();
    // console.log(JSON.stringify(problemInfo));
    // return false;
    var flag = judgeGenerateInfo(problemInfo);
    if (flag == 0)
        return false;

    $.ajax({
        type: "post",
        contentType: "application/json",
        headers: {"atomToken": $("#atomToken").val()},
        data: JSON.stringify(problemInfo),
        url: "http://118.31.113.63:8080/generate",
        success: function (data) {
            // // 清空上次的内容
            // flushContentBefore();
            // $("#modalBody").empty();
            // $("#testPaper").show();
            // $("#testPaper").css("display", "block");
            // 解决异常提示
            if (data.code == 4000) {
                // if (data.errorMap.token != null){
                //     layer.msg("当前页面已过期,请重新刷新页面!")
                //     return false;
                // }
                // console.log(data);
                flushErrorInfo();
                $.each(data.errorMap, function (key, value) {

                    var errorSpan = $("<span class='errorInfo' style='color: red;'></span>").text(value);
                    if (key == "calStyle") {
                        $("#inlineCheckbox4_span").after(errorSpan);
                    } else {
                        $("input[name=" + key + "]").after(errorSpan);
                    }
                })
                $("#atomToken").val(data.atomToken);
                return false;
            }

            if (data.code == 4001) {
                layer.msg(data.msg);
                return false;
            }

            // 异常情况要写在前面
            // 清空上次的内容
            flushContentBefore();
            $("#modalBody").empty();
            renderPaper(data.problemVo);
            // 弹出模态框
            // $('#myModal').modal('show');
            // 清空异常提示
            flushErrorInfo();

        }
    })
}

function flushErrorInfo() {
    $(".errorInfo").remove();
}

function flushContentBefore() {
    // 清空题目
    $("#modalBody").empty();
    // 表名标签还未生成
    if ($("#score").text() == "")
        return;

    // 清空分数
    $("#score").on("click", function () {
        $("#score").remove();
    })

    $("#score").click();
    // todo 清空异常提示
    flushErrorInfo();

}

function judgeGenerateInfo(problemInfo) {
    if (problemInfo.calStyle.length == 0) {
        layer.msg("请选择计算类型");
        return 0;
    }
    // todo 剩下的判断操作
    return obj_is_null(problemInfo);


}

function obj_is_null(obj) {
    for (const key in obj) {
        if (obj.hasOwnProperty(key)) {
            if (obj[key] === null || obj[key] === '') {
                layer.msg("请输入需要生成的题目信息!")
                return 0;
            }
        }
    }
    return 1;
}