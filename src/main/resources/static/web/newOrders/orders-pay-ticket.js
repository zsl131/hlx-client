var moneyBefore = 0;
function buildTicketHtml(obj) {
    moneyBefore = parseFloat($(".show-money").find(".money-amount").find(".money").html()); //先获取最原始的金额
    $.post("/public/json/getPrize", {}, function(res) {
        var orderCount = 0;
        var hideType = '';
        $(".commodity-list-hidden").find("span").each(function() {
            var comNo = $(this).attr("comNo");
            if(comNo=='88888' || comNo == '99999') {
                orderCount ++;
                hideType = comNo=='88888'?"4":"3";
            }
        });
        var html = '<p class="type-name">'+$(obj).html() +'（'+$(obj).attr("title")+'）</small></p>'+
                    '<div class="special-content ticket-list">'+
                    '<input type="hidden" name="commodityCount" value="'+orderCount+'"/>';

        for(var i=0;i<res.length;i++) {
            var ticket = res[i];
            if(ticket.type != hideType) {
                html += '<div class="form-group">'+
                        '<div class="input-group">'+
                            '<div class="input-group-addon">'+ticket.name+'：</div>'+
                            '<input onKeyUp="onInputCount(this)" objType="'+ticket.type+'" worth="'+ticket.worth+'" objId="'+ticket.dataId+'" class="form-control ticket-count" placeholder="请输入对应卡券数量，没有可不输" />'+
                            '<span class="input-group-addon show-member-name">'+ticket.remark+'</span>'+
                        '</div>'+
                    '</div>';
            }
        }
        html += '<div>卡券优惠：<b style="color:#F60;" class="ticket-discount-money">0</b> 元</div>';
        html += '</div>';

        $(".special-type").css("display", "block");
        $(".special-type").html(html);
    });
}

function onInputCount(obj) {
    $(obj).val($(obj).val().replace(/[^\d]/g,''));
    //alert($(obj).attr("name"));
    //checkBossPhone();
    buildTicketDatas();
}

function buildTicketDatas() {

    var commodityCount = parseInt($("input[name='commodityCount']").val());
    var datas = '';
    var ticketCount = 0;
    var breakCount = 0; var dinnerCount = 0; var cardCount = 0; var ticketWorth = 0;//午餐券数量；晚餐券数量；抵价券数量；抵价券价值
    $(".ticket-list").find("input.ticket-count").each(function() {
        var type = $(this).attr("objType");
        var worth = parseInt($(this).attr("worth")); //价值，单位分
        var val = parseInt($(this).val());
        var orderNo = '';
        if(val>0) {
            datas += $(this).attr("objId")+":"+val+"_";
            ticketCount += val;
            if(type=='3') { //午餐券
                breakCount += val;
            } else if(type=='4') { //晚餐券
                dinnerCount += val;
            } else if(type == '2') { //抵价券
                cardCount += val;
                ticketWorth += val*worth;
            }
        }
    });
    //alert(ticketCount+"===="+commodityCount);
    if(ticketCount>commodityCount) {
        showDialog("<p>卡券总数不能超过可抵价商品数量；</p><p>只有全票商品可使用卡券抵价。</p>");
        setReserveDatas('', "卡券总数不能超过可抵价商品数量");
        recalMoney(0, 0, 0);
    } else {
        setReserveDatas(datas, "可提交");
        recalMoney(breakCount, dinnerCount, ticketWorth);
    }
}

function recalMoney(breakCount, dinnerCount, ticketWorth) {
    //alert(breakCount+"==="+dinnerCount+"=="+ticketWorth);
    var totalMoney = 0;
    if(breakCount>0) {
        var price = parseFloat($($(".commodity-list-hidden").find("span[comNo='88888']")[0]).attr("price"));
        var minusMoney = price*breakCount;
        totalMoney += minusMoney;
    }
    if(dinnerCount>0) {
        var price = parseFloat($($(".commodity-list-hidden").find("span[comNo='99999']")[0]).attr("price"));
        var minusMoney = price*dinnerCount;
        totalMoney += minusMoney;
    }
    totalMoney += (ticketWorth/100);
    //alert(totalMoney);
    $(".ticket-discount-money").html(totalMoney);
    var moneyObj = $(".show-money").find(".money-amount").find(".money");
    //var moneyBefore = parseFloat($(moneyObj).html());
    var bondMoney = parseFloat($(".show-money").find(".money-amount").find("small").attr("bondMoney"));
    if(moneyBefore-totalMoney-bondMoney<=0) {
        $(".pay-types").css("display", "none");
    } else {
        $(".pay-types").css("display", "block");
    }
    $(moneyObj).html(moneyBefore-totalMoney);
}

function setReserveDatas(val, title) {
    $("input[name='reserve']").val(val);
    $("input[name='reserve']").attr("title", title);
}
