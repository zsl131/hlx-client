function getCurDate() {
    return new Date();
}

function getSpeDate(spe) {
    var array = spe.split(":");
    var hour = array[0]; var min = array[1];
    var myDate = getCurDate();

    var year = myDate.getFullYear();
    var month = myDate.getMonth();
    var day = myDate.getDate();
    //alert(year+"=="+month+"=="+day+"=="+hour+"=="+min);
    return new Date(year,month, day,hour,min,0);
}

function isDinnerTime(spe) {
    var speDate = getSpeDate(spe);
    var curDate = getCurDate();
    return curDate>speDate;
}