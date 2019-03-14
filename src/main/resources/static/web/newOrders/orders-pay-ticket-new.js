var moneyBefore = 0, cardOrderCount = 0, oldCardNo = '', isDinnerCard; //可用卡券抵价的商品数量、检索过的CardNo、是否是晚餐
function buildTicketHtml(obj) {
    if($(".commodity-list-hidden").find("span[comNo='88888']").length>0 || $(".commodity-list-hidden").find("span[comNo='66666']").length>0) {isDinnerCard = false;} else {isDinnerCard = true} //初始晚餐或午餐
    $(".commodity-list-hidden").find("span").each(function() {
        var comNo = $(this).attr("comNo");
        if(comNo=='88888' || comNo == '99999') {
            cardOrderCount ++;
        }
    });
    moneyBefore = parseFloat($(".show-money").find(".money-amount").find(".money").html()); //先获取最原始的金额
    var html = '<div class="form-group card-main-div">'+
            '<div class="input-group">'+
                '<div class="input-group-addon">卡号：</div>'+
                '<input onKeyUp="onInputCardNo(this)" class="form-control ticket-count" maxLength="7" placeholder="请输入代金券卡号" />'+
                '<span class="input-group-addon show-card-result">输入卡号</span>'+
            '</div>'+
            '<div class="card-add-list" style="padding-top:8px;"></div>'+
            '<div class="worth-money"></div>'+
        '</div>';

    $(".special-type").css("display", "block");
    $(".special-type").html(html);
//    rebuildCardWorth();
//recalMoney();
}

function rebuildCardWorth() {
    var cardCount10=0, cardCount45=0, cardCount55=0, cardCount=0; //全票订单数、10元券数量、45元券数量、55元券数量、卡券总数
    var datas = isDinnerCard?"1-":"0-";
    //console.log(isDinnerCard,datas)
    $(".card-add-list").find("button").each(function() {
        var cardNo = $(this).html();
        if(cardNo.startsWith("1")) {cardCount10 ++;}
        else if(cardNo.startsWith("2")) {cardCount45 ++;}
        else if(cardNo.startsWith("3")) {cardCount55 ++;}
        cardCount ++;
        datas += cardNo+"_";
    });

    if(cardCount>cardOrderCount) {
        showDialog("<p>卡券总数不能超过可抵价商品数量；</p><p>只有全票商品可使用卡券抵价。</p>");
        setReserveDatas('', "卡券总数不能超过可抵价商品数量");
        recalMoney(0, 0, 0);
    } else {
        setReserveDatas(datas, "可提交");
        recalMoney(cardCount10, cardCount45, cardCount55);
    }
}

function setSingleCard(obj, cardNo) {
    var target = $(obj).parents(".card-main-div").find(".card-add-list");
    var checkExistsCardNo = $(target).find("button[cardNo='"+cardNo+"']").attr("cardNo");
    if(typeof(checkExistsCardNo) == "undefined") {
        //console.log($(target).find("button[cardNo='"+cardNo+"']").attr("cardNo"));
        var html = '<button class="btn" onClick="removeSingleCard(this)" style="margin:5px;" cardNo="'+cardNo+'">'+cardNo+'</button>';
        $(target).html(html + $(target).html());
        rebuildCardWorth();
    }
}

function removeSingleCard(obj) {
    var myRemoveCardDialog = confirmDialog("确定移除【"+$(obj).html()+"】这张卡吗？", '<b class="fa fa-warning"></b> 系统提示', function() {
        $(obj).remove();
        $(myRemoveCardDialog).remove();
        rebuildCardWorth();
    })
}

function setCardResult(obj, html, color) {
    var target = $(obj).parents(".card-main-div").find(".show-card-result");
    $(target).html(html);
    $(target).css({"color":color});
}

function onInputCardNo(obj) {
    $(obj).val($(obj).val().replace(/[^\d]/g,''));
    var cardNo = $(obj).val();
    if(cardNo.length==7 && oldCardNo != cardNo) {
        if(!cardNo.startsWith("1") && !cardNo.startsWith("2") && !cardNo.startsWith("3")) {showDialog("输入1、2、3开头的卡号", '<b class="fa fa-warning"></b> 系统提示');} else {
            $.post("/web/card/queryCard", {cardNo: cardNo}, function(res) {
                if(res=='1') {
                    setCardResult(obj, "已添加", "blue");
                    setSingleCard(obj, cardNo);
                } else {
                    setCardResult(obj, res, "red");
                }
                oldCardNo = cardNo; //
            });
        }
    } else if(cardNo.length!=7) {
        setCardResult(obj, "输入卡号", "#777");
    } else if(cardNo==oldCardNo) {
        setCardResult(obj, "已检索", "red");
    }
}

function onInputCount(obj) {
    $(obj).val($(obj).val().replace(/[^\d]/g,''));
    //alert($(obj).attr("name"));
    //checkBossPhone();
    buildTicketDatas();
}

function recalMoney(cardCount10, cardCount45, cardCount55) {
    //alert(breakCount+"==="+dinnerCount+"=="+ticketWorth);
    var needMinusMoney = 0; //是否为晚餐、所抵扣金额
    //console.log("---", isDinnerCard)
//    console.log("isDinner", isDinnerCard);
    var cardListObj = $(".card-add-list");
    var worthObj = $(".worth-money");
    needMinusMoney += cardCount10 * 10; //
    needMinusMoney += cardCount45 * 48; //
    if(isDinnerCard) {
        needMinusMoney += (cardCount55*58)
    } else {needMinusMoney += (cardCount55 *48);} //如果是上午，55元券也只能抵45

    $(worthObj).html("已抵扣：<b>"+needMinusMoney+"</b> 元");
    var moneyObj = $(".show-money").find(".money-amount").find(".money");
    $(moneyObj).html(moneyBefore-needMinusMoney);
}

function setReserveDatas(val, title) {
    $("input[name='reserve']").val(val);
    $("input[name='reserve']").attr("title", title);
}
