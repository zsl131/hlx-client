$(function() {
    init();

    $(".need-rebuild").keyup(function() {
        rebuildMoney();
    });
    rebuildMoney();
})

function init() {
    var wxMoney = parseFloat($("#weixinTotalAM").html()) + parseFloat($("#weixinTotalPM").html()) + parseFloat($("#memberWeixin").html());
    var aliMoney = parseFloat($("#alipayTotalAM").html()) + parseFloat($("#alipayTotalPM").html()) + parseFloat($("#memberAlipay").html());
    $("#weixinMoney").val(wxMoney);
    $("#alipayMoney").val(aliMoney);
}

function rebuildMoney() {
    var totalMoney = getTotalMoney();
    var inMoney = parseFloat($("#inMoney").val());
    inMoney = isNaN(inMoney)?0:inMoney;
    var payMoney = parseFloat($("#payMoney").val());
    payMoney = isNaN(payMoney)?0:payMoney;

    var wxMoney = parseFloat($("#weixinTotalAM").html()) + parseFloat($("#weixinTotalPM").html());
    var aliMoney = parseFloat($("#alipayTotalAM").html()) + parseFloat($("#alipayTotalPM").html());

    var weixinMoney = parseFloat($("#weixinMoney").val());
    weixinMoney = isNaN(weixinMoney)?0:weixinMoney;
    var alipayMoney = parseFloat($("#alipayMoney").val());
    alipayMoney = isNaN(alipayMoney)?0:alipayMoney;

//console.log(aliMoney+"===="+alipayMoney);

    var money = totalMoney + (wxMoney-weixinMoney) + (aliMoney-alipayMoney) + inMoney - payMoney;
    setResultMoney(money);
}

function setResultMoney(money) {
    $("#calTotalMoney").html(money + " 元");
}

function getTotalMoney() {
    var memberTotalMoney = parseFloat($("#memberTotalMoney").html()); //会员充值
    var totalMoneyAM = parseFloat($("#totalMoneyAM").html()); //上午合计
    var totalMoneyPM = parseFloat($("#totalMoneyPM").html()); //下午合计

    var totalMoney = memberTotalMoney + totalMoneyAM + totalMoneyPM;
    return totalMoney;
}