$(function() {
    $("input[name='bossPhone']").keyup(function() {
        checkAdminPhoneOnKeyup(this);
    });
});

function submitFriendOrder(obj) {
    var bossPhone = $("#bossPhoneHidden").val();
    var pObj = $(obj).parents(".content-box");
    var peopleCount = parseInt($(pObj).find("input[name='peopleCount']").val()); //全票
    var halfCount = parseInt($(pObj).find("input[name='halfCount']").val()); //半票
    var childCount = parseInt($(pObj).find("input[name='childCount']").val()); //免票
    var bondMoney = parseFloat($("b.bondMoney").html())*peopleCount;
    var level = $("#isDinner").val()=='1'?"1":"2"; //晚餐或午餐
    var price = getPrice(); //单价
    if(!bossPhone) {
        showDialog("请先输入老板电话进行验证");
    } else if(!peopleCount || peopleCount<=0) {
        showDialog("请输入全票人数");
    } else if((halfCount!=0 && !halfCount) || halfCount<0) {
        showDialog("请输入半票人数，没有请输入0");
    } else if((childCount!=0 && !childCount) || childCount<0) {
        showDialog("请输入儿童人数，没有请输入0");
    } else {
        setSubmitBtnStyle(obj, true);
        $.post("/web/orders/addFriendDiscountOrder", {level:level, bondMoney:bondMoney, phone:bossPhone, peopleCount:peopleCount, price:price, halfCount:halfCount, childCount:childCount}, function(res) {
            alert(res.msg);
            setSubmitBtnStyle(obj, false);
            window.location.href = '/web/orders/show?no='+res.code;
        }, "json");
    }
}

function checkAdminPhoneOnKeyup(obj) {
    var phone = $(obj).val();
    if(isPhone(phone)) {
        checkAdminPhone();
    } else {
        setAdminPhoneValue("");
        $(".show-boss-name").html("");
    }
}

function checkAdminPhone() {
    var phoneObj = $("input[name='bossPhone']");
    var phone = $(phoneObj).val();
    if(isPhone(phone)) {
        $.post("/public/json/getAdminPhone", {phone:phone}, function(res) {
            if(res.id) {
                var html = res.name;
                $(".show-boss-name").html(html);
                $(".show-boss-name").css({'color':'#F00',"font-weight":'bold'});
                setAdminPhoneValue(res.phone);
            } else {
                showDialog("<i class='fa fa-close'></i> 输入的手机号码【"+phone+"】无友情价！", "<i class='fa fa-info-circle'></i> 系统提示");
                //$(phoneObj).focus(); //会一直接弹窗
                setAdminPhoneValue("");
                $(".show-boss-name").html("");
            }
        }, "json");
    } else {
        showDialog("<i class='fa fa-close'></i> 输入手机号码有误，请核对！", "<i class='fa fa-info-circle'></i> 系统提示");
        //$(phoneObj).focus(); //会一直接弹窗
        setAdminPhoneValue("");
        $(".show-boss-name").html("");
    }
}

function setAdminPhoneValue(val) {
    $("#bossPhoneHidden").val(val);
}