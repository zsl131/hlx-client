$(function() {
    setMaxHeight();
    setCommodityStyle();
})

function setCommodityStyle() {
    var spe = $("input[name='mealTime']").val();
    if(!spe) {return;}
    var isDinner = isDinnerTime(spe);
    //alert(isDinner);
    $(".index-commodity-list").find("ul").find("li").each(function() {
        $(this).attr("canBuy", 1);
        var comType = $(this).attr("comType");
        var price = parseFloat($(this).find(".price").html());
        if(comType!='3' && (((isDinner && comType!='2') || (!isDinner && comType!='1')) && price>0)) { //时段不对
            setComStyle($(this));
        }
    })
}

function setComStyle(obj, canBuy) {
    if(!canBuy) {
        $(obj).css({"background":"#f18f8d", "cursor":"not-allowed"});
        $(obj).attr("canBuy", 0);
        $(obj).attr("title", "此商品不可在此时段购买");
    } else {
        $(obj).css({"background":"#c13b38", "cursor":"normal"});
        $(obj).attr("canBuy", 1);
        $(obj).removeAttr("title");
    }
}

function setMaxHeight() {
    var winHeight = $(window).height();
    var needHeight = winHeight - 60 - 100;

    $(".max-height").each(function() {
        $(this).css("height", needHeight+"px");
    });
}

function setWidth(obj) {
    var width = $(obj).find(".left").width();
    var centerObj = $(obj).find(".center");
    var needWidth = width - 30 -30-1;
//    alert(needWidth);
    $(centerObj).css("width", needWidth+"px");
}