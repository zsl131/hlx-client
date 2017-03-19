var memberSurplus = 0; //会员剩余金额
$(function() {
    $("input[name='memberPhone']").keyup(function() {
        checkPhoneOnKeyup(this);
    });
});

function calMemberMoney(obj) {
    $(obj).val($(obj).val().replace(/[^\d]/g,''));
    //alert($(obj).attr("name"));
    calMenberOrder(obj);
}

function calMenberOrder(obj) {
    var pObj = $(obj).parents(".content-box");
    var peopleCount = parseInt($(pObj).find("input[name='peopleCount']").val());
    var halfCount = parseInt($(pObj).find("input[name='halfCount']").val());

    peopleCount=(!peopleCount||peopleCount<=0)?0:peopleCount;
    halfCount=(!halfCount||halfCount<=0)?0:halfCount;
    var price = getPrice();
    var bondMoney = parseFloat($(".bondMoney").html());
    $(pObj).find(".meal").html(price*peopleCount);
    $(pObj).find(".halfMeal").html(price*halfCount*0.5);
    $(pObj).find(".bond").html(bondMoney*peopleCount);

    var totalMoney =price*peopleCount+price*halfCount*0.5;
    $(pObj).find(".total").html(totalMoney);
    var diffMoney = ((memberSurplus-totalMoney*100))/100; //差额，如果小于0表示要补钱

    $(pObj).find(".diffMoney").html((diffMoney<0?0-diffMoney:0));
    $(pObj).find(".need").html(bondMoney*peopleCount+(diffMoney<0?0-diffMoney:0));
}

function submitMemberOrder(obj) {
    var memberPhone = $("#memberPhoneHidden").val();
    var pObj = $(obj).parents(".content-box");
    var peopleCount = parseInt($(pObj).find("input[name='peopleCount']").val()); //全票
    var halfCount = parseInt($(pObj).find("input[name='halfCount']").val()); //半票
    var childCount = parseInt($(pObj).find("input[name='childCount']").val()); //免票
    var bondMoney = parseFloat($("b.bondMoney").html())*peopleCount;
    var level = $("#isDinner").val()=='1'?"1":"2"; //晚餐或午餐
    var price = getPrice(); //单价
    var payType = $(pObj).find("input[name='payType']").val();
    if(!memberPhone) {
        showDialog("请先输入顾客手机号码进行验证");
    } else if(!peopleCount || peopleCount<=0) {
        showDialog("请输入全票人数");
    } else if((halfCount!=0 && !halfCount) || halfCount<0) {
        showDialog("请输入半票人数，没有请输入000");
    } else if((childCount!=0 && !childCount) || childCount<0) {
        showDialog("请输入儿童人数，没有请输入0");
    } else if(!payType) {
         showDialog("请选择顾客付款方式");
     }  else {
        setSubmitBtnStyle(obj, true);
        $.post("/web/orders/addMemberOrder", {level:level, bondMoney:bondMoney, phone:memberPhone, peopleCount:peopleCount, price:price, halfCount:halfCount, childCount:childCount, payType:payType}, function(res) {
            alert(res.msg);
            setSubmitBtnStyle(obj, false);
            window.location.href = '/web/orders/show?no='+res.code;
        }, "json");
    }
}

function checkPhoneOnKeyup(obj) {
    var phone = $(obj).val();
    if(isPhone(phone)) {
        checkPhone();
    } else {
        setPhoneValue("");
        $(".show-member-name").html("请输入");
        memberSurplus =0;
    }
}

function checkPhone() {
    var phoneObj = $("input[name='memberPhone']");
    var phone = $(phoneObj).val();
    if(isPhone(phone)) {
        $.post("/public/json/getMember", {phone:phone}, function(res) {
            if(res.id) {
                var html = res.name;
                $(".show-member-name").html(html);
                $(".show-member-name").css({'color':'#F00',"font-weight":'bold'});
                setPhoneValue(res.phone);
                memberSurplus = res.surplus; //会员剩余金额
                $(".member-info").html("会员信息："+res.name+"，剩余："+(res.surplus/100)+" 元");
            } else {
                showDialog("<i class='fa fa-close'></i> 输入的手机号码【"+phone+"】查无会员信息！");
                //$(phoneObj).focus(); //会一直接弹窗
                setPhoneValue("");
                $(".show-member-name").html("");
                memberSurplus =0;
            }
        }, "json");
    } else {
        showDialog("<i class='fa fa-close'></i> 输入手机号码有误，请核对！");
        //$(phoneObj).focus(); //会一直接弹窗
        setPhoneValue("");
        $(".show-member-name").html("");
        memberSurplus =0;
    }
}

function setPhoneValue(val) {
    $("#memberPhoneHidden").val(val);
}