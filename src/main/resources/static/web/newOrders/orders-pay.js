$(function() {
    buildCom();
    buildTotalMoney();
    $(".order-type-div").find("button").click(function() {
        onOrderType($(this));
    });
    $(".pay-types").find("button").click(function() {
        onPayType($(this));
    });
});

function onOrderType(obj) {
    $(".order-type-div").find("button").each(function() {
        $(this).removeClass("btn-info")
    });
    var remindTitle = "信息不全，不可以提交";
    var initValue = "";
    $(obj).addClass("btn-info");
    var orderType = $(obj).attr("orderType");
    $("input[name='specialType']").val(orderType);
    if(orderType =='5') { //会员订单
        buildMemberHtml(obj);
        remindTitle = "请先输入会员手机号码进行验证";
        initValue = "";
    } else if(orderType == '1') { //普通订单
        buildNormalHtml(obj); initValue = "1"; //普通订单时可以直接提交
    } else if(orderType == '4') { //友情折扣订单
        buildFriendHtml(obj);
        remindTitle = "请先输入折扣手机号码进行验证，待审核通过后方可提交";
        initValue = "";
    }
    setReserveInfo(initValue, remindTitle);
}

function setReserveInfo(value, title) {
    $("input[name='reserve']").attr("title", title);
    $("input[name='reserve']").val(value);
}

//String no, Float bondMoney, Integer bondCount, String payType,
//                                          String specialType, String reserve
function submitOrder() {
    var no = $("input[name='orderNo']").val();
    var bondMoney = $("input[name='bondMoney']").val();
    var bondCount = $("input[name='bondCount']").val();
    var payType = $("input[name='payType']").val();
    var specialType = $("input[name='specialType']").val();
    var reserve = $("input[name='reserve']").val();

    var remindTitle = $("input[name='reserve']").attr("title");
    if($.trim(reserve)=='') {
        showDialog(remindTitle);
    } else {
        var html = '<i class="fa fa-info"></i> 确定已收款并提交订单吗？';
        var submitDialog = confirmDialog(html, '<i class="fa fa-info-circle"></i> 系统提示', function() {
            $.post("/web/newOrders/postOrder", {no:no, bondMoney:bondMoney, bondCount:bondCount, payType:payType, specialType:specialType, reserve:reserve}, function(res) {
                alert(res.msg);
                window.location.reload();
            }, "json");
        });
    }
}

function onPayType(obj) {
    $(".pay-types").find("button").each(function() {
        $(this).removeClass("btn-danger");
    });
    $(obj).addClass("btn-danger");
    $("input[name='payType']").val($(obj).attr("payType"));
}

function buildTotalMoney() {
    var count = parseInt($("input[name='bondCount']").val());
    var price = parseFloat($("input[name='bondMoney']").val());
    var tarObj = $(".money-amount");
    var moneyObj = $(tarObj).find(".money");
    var remindObj = $(tarObj).find("small");
    $(moneyObj).html(parseFloat($(moneyObj).html()) + count*price);
    $(remindObj).html("（包含"+(count*price)+" 元压金，只收取全票人的压金）");
}


function isPhone(sMobile) {
    if((/^1[3|4|5|8][0-9]\d{8}$/.test(sMobile))) {
        return true;
    } else {return false;}
}