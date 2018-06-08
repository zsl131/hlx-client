function buildScoreHtml(obj) {
    var html = '<p class="type-name">'+$(obj).html() +'（'+$(obj).attr("title")+'）</small></p>'+
                    '<div class="special-content">'+
                        '<div class="form-group">'+
                            '<div class="input-group">'+
                                '<div class="input-group-addon">用户电话：</div>'+
                                '<input name="walletPhone" type="text" maxlength="11" tabindex="1" onKeyUp="onWalletPhone(this)" class="form-control" placeholder="由顾客提供微信平台上绑定的手机号码" />'+
                                '<span class="input-group-addon show-wallet-name">请输入</span>'+
                            '</div>'+
                        '</div>'+
                        '<div class="form-group">'+
                            '<div class="input-group">'+
                                '<div class="input-group-addon">支付密码：</div>'+
                                '<input name="payPassword" type="text" maxlength="4" tabindex="2" onKeyUp="onWalletPassword(this)" class="form-control" placeholder="询问顾客支付密码，可在公众号“我”-“消费密码”处查看或修改" />'+
                                '<span class="input-group-addon show-wallet-password">请输入</span>'+
                            '</div>'+
                        '</div>'+
                        '<div class="form-group">'+
                            '<div class="input-group">'+
                                '<div class="input-group-addon">积分剩余：</div>'+
                                '<input class="form-control" name="scoreSurplus" value="输入用户电话和密码检索" readonly="readonly"/>'+
                            '</div>'+
                        '</div>'+

                        '<div class="score-info-show"></div>'+
                    '</div>';

    $(".special-type").css("display", "block");
    $(".special-type").html(html);
    $("input[name='walletPhone']").focus();
}

function onWalletPhone(obj) {
    $(obj).val($(obj).val().replace(/[^\d]/g,''));
    //alert($(obj).attr("name"));
    checkWalletPhone();
}

function onWalletPassword(obj) {
    var password = $("input[name='payPassword']").val();
    if(password.length == 4) {
        checkWalletPhone();
    } else {
        $(".show-member-password").html("请认真输入");
    }
}

function checkWalletPhone() {
    var phoneObj = $("input[name='walletPhone']");
    var phone = $(phoneObj).val();
    if(isPhone(phone)) {
        $.post("/public/json/getWallet", {phone:phone}, function(res) {
            if(res.id) {
                var password = $("input[name='payPassword']").val();
                if(password != res.password) {
                    $(".show-wallet-password").html("密码输入错误");
                    $(".show-wallet-password").css({'color':'#F00',"font-weight":'bold'});
                } else {
                    var html = res.name;
                    $("input[name='scoreSurplus']").val(res.score+" 分，相当于人民币："+(parseInt(res.score/parseInt($('input[name="scoreMoney"]').val())))+" 元");
                    $(".show-wallet-name").html("检索成功");
                    $(".show-wallet-password").html("密码正确");
                    buildScoreMoney(res.score);
                    //$("input[name='reserve']").val(res.phone);
                    setReserveInfo(res.phone, "可以提交");
                }
            } else {
                //showDialog("<i class='fa fa-close'></i> 输入的手机号码【"+phone+"】查无会员信息！");
                $(".show-wallet-name").html("查无此号码信息");
                $(".show-wallet-name").css({'color':'#F00',"font-weight":'bold'});
            }
        }, "json");
    } else {
        $(".show-wallet-name").html("请认真输入");
        $("input[name='scoreSurplus']").val("-");
    }
}

function buildScoreMoney(memberSurplus) {
    var orderMoney = $("input[name='orderTotalMoney']").val();
    var discountMost = parseInt(parseInt(orderMoney) * parseInt($('input[name="scoreDeductible"]').val()) /100);
    var scoreMoney = parseInt(memberSurplus/parseInt($('input[name="scoreMoney"]').val())); //账户可抵
    var realDiscount = discountMost>scoreMoney?scoreMoney:discountMost;//实际抵扣

//    alert(discountMost+"===="+scoreMoney+"==="+realDiscount);
//    alert(discountMost>scoreMoney);

    var html = '<p>订单金额：<b>'+ orderMoney +'</b>，最高可抵：<b>' + discountMost + '</b>，实际可抵：<b>'+ realDiscount +'</b></p>'+
                '<p>抵扣后订单金额为：<b>'+(orderMoney-realDiscount)+'</b> 元，积分剩余：<b>'+(memberSurplus-realDiscount*parseInt($('input[name="scoreMoney"]').val()))+'</b> 分</p>';

    /*var html = '消费信息：<b>'+memberSurplus+'</b>（账户余额）- '+
                '<b>'+orderMoney+'</b>（订单金额）= '+
                '<b><i class="fa fa-cny"></i> '+(memberSurplus-orderMoney)+'</b>（消费后剩余）';*/
    $(".score-info-show").html(html);

    buildScoreNeedMoney(orderMoney, realDiscount);
}

function buildScoreNeedMoney(orderMoney, realDiscount) {
    var payMoney = orderMoney - realDiscount;
    var bondMoney = parseFloat($("input[name='bondMoney']").val());
    var bondCount = parseInt($("input[name='bondCount']").val());
//    var needMoney = bondMoney * bondCount;
//    var needMoney = (bondCount>=2)?bondMoney:0; //押金
    var needMoney = (bondCount<2)?0:(bondCount<5?bondMoney:50); //押金
    var surplus = payMoney + needMoney;

    $(".pay-types").css("display", "block");

    if(needMoney<=0) {
        $(".bond-pay-types").css("display", "none");
    } else {
        $(".bond-pay-types").css("display", "block");
    }

    //$(moneyObj).html(parseFloat($("#totalMoney").html()) + needMoney);
    $(".show-money").find(".money-amount").find(".money").html(needMoney+payMoney);
    $(".show-money").find(".money-amount").find(".money").find("small").html("（包含"+((bondCount>=2)?bondMoney:0)+" 元押金，只收取全票人的押金）");
}