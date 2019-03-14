var remindTitle = "信息不全，不可以提交";
var initOrderType = 0;

$(function() {
    initOrderType = $("input[name='specialType']").val(); //订单类型
    if(initOrderType!='1') {
        $(".order-type-div").find("button").each(function() {
            $(this).attr("disabled", "disabled");
        });
    }

    buildTotalMoney();
    $(".order-type-div").find("button").click(function() {
        onOrderType($(this));
    });
    $(".pay-types").find("button").click(function() {
        onPayType($(this));
    });

    $(".bond-pay-types").find("button").click(function() {
            onBondPayType($(this));
    });
    buildNormalHtml();
    buildDiscountHtml();
    onFriendOrder();
});

function loadOrderTimer() {
    setInterval(function() {
        var orderNo = $("input[name='orderNo']").val();
        $.get("/web/newOrders/queryOrder", {orderNo:orderNo}, function(res) {
            if(res.status!='0') {window.location.reload();}
        }, "json");
    }, "1000");
}

function onFriendOrder() {
    var specialType = $("input[name='specialType']").val();
    if(specialType=='4') {
        buildFriendHtml($("button[orderType='4']"));
        remindTitle = "请先输入折扣手机号码进行验证，待审核通过后方可提交";
        var discountReason = $("input[name='discountReason']").val();
        var orderStatus = $("input[name='orderStatus']").val();
        $("input[name='bossPhone']").val(discountReason);
        var initValue = "";
        if(orderStatus=='6') {
            $(".member-info-show").html("已优惠 <b style='color:#F60; font-size:16px;'>"+$("input[name='discountMoney']").val() +"</b> 元");
        }
        if(orderStatus!='6') {
            remindTitle = "请等待【"+discountReason+"】确认后才可享受折扣哦！";
            $(".submit-order-btn").html("<i class='fa fa-recycle'></i> 请等待确认");
            $(".submit-order-btn").attr("disabled", "disabled");
            $("input[name='reserve']").val("");
            loadOrderTimer();
        } else {
            initValue = discountReason;
            $("input[name='reserve']").val(discountReason);
        }
        setReserveInfo(initValue, remindTitle);
    }
}

function onOrderType(obj) {
    $(".order-type-div").find("button").each(function() {
        $(this).removeClass("btn-info")
    });

//    $(".show-money").find(".money-amount").find(".money").html($("input[name='needMoney']").val()); //每次切换都重新显示原始金额
    buildTotalMoney();

    var initValue = "";
    $(obj).addClass("btn-info");
    var orderType = $(obj).attr("orderType");
    $("input[name='specialType']").val(orderType);
    if(orderType =='5') { //会员订单
        buildMemberHtml(obj);
        remindTitle = "请先输入会员手机号码和密码进行验证";
        initValue = "";
    } else if(orderType == '11') { //积分抵扣订单
        buildScoreHtml(obj);
        remindTitle = "请先输入用户手机号码和密码进行验证";
        initValue = "";
    } else if(orderType == '1') { //普通订单
        buildNormalHtml(obj);
        buildDiscountHtml(obj);
        initValue = "1"; //普通订单时可以直接提交
    } else if(orderType == '4') { //友情折扣订单
        buildFriendHtml(obj);
        remindTitle = "请先输入折扣手机号码进行验证，待审核通过后方可提交";
        initValue = "";
    } else if(orderType == '3') { //美团订单
        var net = $(obj).attr("net");
        if(net=='1') {
            buildMtHtml(obj);
            remindTitle = "请先输入美团编码";
            initValue = "";
        } else {
            buildMtHtmlNoNet(obj);
            remindTitle = "请先输入美团编码";
            initValue = "";
        }
    } else if(orderType == '6') { //卡券订单
        buildTicketHtml(obj);
        remindTitle = "请先设置对应卡券数量";
        initValue = "";
    } else if(orderType == '9') { //飞凡订单
        buildFfanHtml(obj);
        remindTitle = "请先输入飞凡提货码或券码";
        initValue = '';
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
    var bondPayType = $("input[name='bondPayType']").val();
    var specialType = $("input[name='specialType']").val();
    var reserve = $("input[name='reserve']").val();

    var remindTitle = $("input[name='reserve']").attr("title");
    if($.trim(reserve)=='') {
        showDialog(remindTitle);
    } else {
        var html = '<i class="fa fa-info"></i> 确定已收款并提交订单吗？';
        var submitDialog = confirmDialog(html, '<i class="fa fa-info-circle"></i> 系统提示', function() {
            $.post("/web/newOrders/postOrder", {no:no, bondPayType:bondPayType, bondMoney:bondMoney, bondCount:bondCount, payType:payType, specialType:specialType, reserve:reserve}, function(res) {
//                alert(res.msg);
                window.location.reload();
            }, "json");

            setSubmitBtnStyle();
            $(submitDialog).remove(); //直接关闭
        });
    }
}

function setSubmitBtnStyle() {
    $(".submit-remind-btn").css("display", "block");
    $(".submit-order-btn").css("display", "none");
}

function removeOrder() {
    var no = $("input[name='orderNo']").val();
    var status = $("input[name='orderStatus']").val();

    if(status!='0') {
        showDialog("只有在刚下单状态下才可取消订单");
    } else {
        var html = '<i class="fa fa-question-circle"></i> 此操作不可逆，确定要删除该订单吗？';
        var submitDialog = confirmDialog(html, '<i class="fa fa-info-circle"></i> 系统提示', function() {
            $.post("/web/newOrders/removeOrder", {no:no}, function(res) {
                window.location.reload();
            }, "json");

            $(submitDialog).remove(); //直接关闭
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

function onBondPayType(obj) {
    $(".bond-pay-types").find("button").each(function() {
        $(this).removeClass("btn-danger");
    });
    $(obj).addClass("btn-danger");
    $("input[name='bondPayType']").val($(obj).attr("payType"));
}

function buildTotalMoney() {
    var count = parseInt($("input[name='bondCount']").val());
    var price = parseFloat($("input[name='bondMoney']").val()); //之前是每个全票收取的费用，目前是每桌本票人数不小于2时的费用
    var tarObj = $(".money-amount");
    var moneyObj = $(tarObj).find(".money");
    var remindObj = $(tarObj).find("small");

//    var needBondMoney = (count>=2)?price:0;//押金
//    var needBondMoney = (count<2)?0:(count<5?price:50);//押金
    var needBondMoney = (count<1)?0:(count==1?10:(count<5?price:50)); //押金

//    console.log(needBondMoney);

    $(moneyObj).html(parseFloat($("#totalMoney").html()) + needBondMoney);
    $(remindObj).html("（包含"+(needBondMoney)+" 元押金）");
    $(remindObj).attr("bondMoney", needBondMoney);
//    $(moneyObj).html(parseFloat($("#totalMoney").html()) + count*price);
//    $(remindObj).html("（包含"+(count*price)+" 元押金）");

    if(parseFloat($("#totalMoney").html())<=0) {
        $(".pay-types").css("display", "none");
    } else {
        $(".pay-types").css("display", "block");
    }

    if(needBondMoney<=0) {
        $(".bond-pay-types").css("display", "none");
    } else {
        $(".bond-pay-types").css("display", "block");
    }

    $("input[name='bondMoney']").val(needBondMoney); //重设押金金额
}

function isPhone(sMobile) {
    /*if((/^1[3|4|5|7|8][0-9]\d{8}$/.test(sMobile))) {
        return true;
    } else {return false;}*/
    if(sMobile.length!=11 || !/^[0-9]+$/.test(sMobile)){
        return false;
    }
    return true;
}