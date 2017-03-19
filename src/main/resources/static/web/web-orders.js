$(function() {
    $("input[name='peopleCount']").focus();

    checkIsDinnerInterval();

    $('.pay-type').find("button").click(function() {
        setBtnStyle(this);
    });
});

function setSubmitBtnStyle(obj, wait) {
    if(wait) {
        $(obj).attr("disabled", true);
        $(obj).html("<i class='fa fa-spinner'></i> 提交中…");
    } else {
        $(obj).removeAttr("disabled");
        $(obj).html("<i class='fa fa-gavel'></i> 确定提交");
    }
}

function setBtnStyle(obj) {
    $('.pay-type').find("button").each(function() {
        $(this).removeClass("btn-info")
    });

    $(obj).addClass("btn-info");
    $("input[name='payType']").val($(obj).val());
}

function calMoney(obj) {
    $(obj).val($(obj).val().replace(/[^\d]/g,''));
    //alert($(obj).attr("name"));
    calOrder(obj);
}

function calOrder(obj) {
    var pObj = $(obj).parents(".main-form-box");
    var peopleCount = parseInt($(pObj).find("input[name='peopleCount']").val());
    var halfCount = parseInt($(pObj).find("input[name='halfCount']").val());

    peopleCount=(!peopleCount||peopleCount<=0)?0:peopleCount;
    halfCount=(!halfCount||halfCount<=0)?0:halfCount;
    var price = getPrice();
    var bondMoney = parseFloat($("b.bondMoney").html());
    $(pObj).find(".meal").html(price*peopleCount);
    $(pObj).find(".halfMeal").html(price*halfCount*0.5);
    $(pObj).find(".bond").html(bondMoney*peopleCount);
    $(pObj).find(".total").html(price*peopleCount+price*halfCount*0.5+bondMoney*peopleCount);
}

function getPrice() {
    var isDinner = $("#isDinner").val();
    if(isDinner=='0') {
        return parseFloat($(".breakfastPrice").html());
    } else {
        return parseFloat($(".dinnerPrice").html());
    }
}

function checkIsDinnerInterval() {
    checkIsDinner(); //先执行一次，再定时
    setInterval(checkIsDinner, "1000");
}

function checkIsDinner() {
    var spe = $(".mealTime").html();
    var isDinner = isDinnerTime(spe);
    //alert(spe+"==="+isDinner);
    if(isDinner) {
        $(".mealName").html("晚餐");
        $("#isDinner").val("1");
    } else {
        $(".mealName").html("午餐");
        $("#isDinner").val("0");
    }
}

function isPhone(sMobile) {
    if((/^1[3|4|5|8][0-9]\d{8}$/.test(sMobile))) {
        return true;
    } else {return false;}
}