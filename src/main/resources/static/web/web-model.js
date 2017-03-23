$(function() {
    queryPrice();
    queryRules();
});

function queryPrice() {
    $.get("/public/json/getPrice",{},function(res) {
        if(res) {
            $("b.breakfastPrice").html(res.breakfastPrice);
            $("b.dinnerPrice").html(res.dinnerPrice);
            $(".bondMoney").html(res.bondMoney);
        }
    }, "json");
}

function queryRules() {
    $.get("/public/json/getRules",{},function(res) {
        if(res) {
            var friendPercent = res.friendPercent;
            if(friendPercent>0 && friendPercent<100) {
                //$("b.friendPercent").html(friendPercent*1.0/10);
                if($(".mealTime")) {$(".mealTime").html(res.spe);}
            }
        }
    }, "json");
}