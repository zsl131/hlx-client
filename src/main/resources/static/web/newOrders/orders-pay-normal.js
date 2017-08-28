function buildNormalHtml() {
    var html = '';
    var discountMoney = parseFloat($("input[name='discountMoney']").val());
    var discountReason = $("input[name='discountReason']").val();
    if(discountMoney>0) {
        html = '<p class="type-name">此订单已优惠：<b style="color:#F00;">'+discountMoney+' 元</b>，原因：'+discountReason+'</p>';
        $(".special-type").css("display", "block");
        var needMoney = parseFloat($(".show-money").find(".money-amount").find(".money").html())-discountMoney;
        //alert(needMoney);
        $(".show-money").find(".money-amount").find(".money").html(needMoney);
    } else {
        html = '';
        $(".special-type").css("display", "none");
    }
    $(".special-type").html(html);
    setReserveInfo("1", "可以提交");
}
