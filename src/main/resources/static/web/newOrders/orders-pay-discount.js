function buildDiscountHtml(obj) {
    var html = '';
    var isDiscountDay = $("input[name='isDiscountDay']").val()=='1';
    var configObj = $("input[name='discountConfig']");
    var am = parseFloat(configObj.attr("am")), pm = parseFloat(configObj.attr("pm")), amh = parseFloat(configObj.attr("amh")), pmh = parseFloat(configObj.attr("pmh"));
    var discountMoney = parseFloat($("input[name='discountMoney']").val());
    var discountReason = $("input[name='discountReason']").val();
    if(isDiscountDay && discountMoney>0) {
        html = '<p class="type-name">今天是<b style="color:#F00;">【折扣日】</b>，上午优惠：<b style="color:#F00">'+am+'元/全票</b>，<b style="color:#F00">'+amh+'元/半票</b>，下午优惠：<b style="color:#F00">'+pm+'元/全票</b>，<b style="color:#F00">'+pmh+'元/半票</b></p>'+
                '<p>总价已优惠：<b style="color:#F00; font-size:20px;padding:0px 6px;">'+discountMoney+'</b>元</p>';
        $(".special-type").css("display", "block");

        //var needMoney = parseFloat($(".show-money").find(".money-amount").find(".money").html())-discountMoney;
        //alert(needMoney);
        //$(".show-money").find(".money-amount").find(".money").html(needMoney);
    } else {
        html = '';
        $(".special-type").css("display", "none");
    }
    $(".special-type").html(html);
    setReserveInfo("1", "可以提交");
}
