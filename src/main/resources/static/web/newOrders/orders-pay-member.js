function buildMemberHtml(obj) {
    var html = '<p class="type-name">'+$(obj).html() +'（'+$(obj).attr("title")+'）</small></p>'+
                    '<div class="special-content">'+
                        '<div class="form-group">'+
                            '<div class="input-group">'+
                                '<div class="input-group-addon">会员电话：</div>'+
                                '<input name="memberPhone" type="text" maxlength="11" tabindex="1" onKeyUp="onMemberPhone(this)" class="form-control" placeholder="由顾客提供所留手机号码" />'+
                                '<span class="input-group-addon show-member-name">请输入</span>'+
                            '</div>'+
                        '</div>'+
                        '<div class="form-group">'+
                            '<div class="input-group">'+
                                '<div class="input-group-addon">会员姓名：</div>'+
                                '<input class="form-control" name="memberName" value="输入会员电话检索" readonly="readonly"/>'+
                            '</div>'+
                        '</div>'+
                        '<div class="form-group">'+
                            '<div class="input-group">'+
                                '<div class="input-group-addon">账户余额：</div>'+
                                '<input class="form-control" name="memberSurplus" value="输入会员电话检索" readonly="readonly"/>'+
                            '</div>'+
                        '</div>'+

                        '<div class="member-info-show"></div>'+
                    '</div>';

    $(".special-type").css("display", "block");
    $(".special-type").html(html);
    $("input[name='memberPhone']").focus();
}

function onMemberPhone(obj) {
    $(obj).val($(obj).val().replace(/[^\d]/g,''));
    //alert($(obj).attr("name"));
    checkPhone();
}

function checkPhone() {
    var phoneObj = $("input[name='memberPhone']");
    var phone = $(phoneObj).val();
    if(isPhone(phone)) {
        $.post("/public/json/getMember", {phone:phone}, function(res) {
            if(res.id) {
                var html = res.name;
                $("input[name='memberName']").val(html);
                $("input[name='memberSurplus']").val(res.surplus/100+" 元");
                $(".show-member-name").html("检索成功");
                buildMemberMoney(res.surplus/100);
                //$("input[name='reserve']").val(res.phone);
                setReserveInfo(res.phone, "可以提交");
            } else {
                //showDialog("<i class='fa fa-close'></i> 输入的手机号码【"+phone+"】查无会员信息！");
                $(".show-member-name").html("此号码无会员");
                $(".show-member-name").css({'color':'#F00',"font-weight":'bold'});
            }
        }, "json");
    } else {
        $(".show-member-name").html("请认真输入");
        $("input[name='memberName']").val("-");
        $("input[name='memberSurplus']").val("-");
    }
}

function buildMemberMoney(memberSurplus) {
    var orderMoney = $("input[name='orderTotalMoney']").val();
    var html = '消费信息：<b>'+memberSurplus+'</b>（账户余额）- '+
                '<b>'+orderMoney+'</b>（订单金额）= '+
                '<b><i class="fa fa-cny"></i> '+(memberSurplus-orderMoney)+'</b>（消费后剩余）';
    $(".member-info-show").html(html);

    buildMemberNeedMoney(memberSurplus);
}

function buildMemberNeedMoney(memberSurplus) {
    var orderMoney = $("input[name='orderTotalMoney']").val();
    var surplus = memberSurplus - orderMoney;
    var bondMoney = parseFloat($("input[name='bondMoney']").val());
    var bondCount = parseInt($("input[name='bondCount']").val());
    var needMoney = bondMoney * bondCount;
    if(surplus<0) {
        needMoney = needMoney - surplus;
    }

    $(".show-money").find(".money-amount").find(".money").html(needMoney);
}