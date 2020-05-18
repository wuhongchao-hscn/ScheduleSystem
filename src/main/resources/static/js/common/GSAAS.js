function hCopy(fObj) {
    if (fObj.strScheduleEndHmH.value == "") {
        let strScheduleStartHmH = fObj.strScheduleStartHmH.value;
        let checkScheduleStartHmH = strScheduleStartHmH.match(/^([0-9]|[0-9][0-9])$/);
        if (checkScheduleStartHmH) {
            fObj.strScheduleEndHmH.value = strScheduleStartHmH;
        }
    }
}

function mCopy(fObj) {
    if (fObj.strScheduleEndHmM.value == "") {
        let strScheduleStartHmM = fObj.strScheduleStartHmM.value;
        let checkScheduleStartHmM = strScheduleStartHmM.match(/^([0-9]|[0-9][0-9])$/);
        if (checkScheduleStartHmM) {
            fObj.strScheduleEndHmM.value = strScheduleStartHmM;
        }
    }
}

function setSysDate() {
    let sysDate = new Date();
    $("#strScheduleYykYmdY").val(sysDate.getFullYear());
    $("#strScheduleYykYmdM").val(sysDate.getMonth() + 1);
    $("#strScheduleYykYmdD").val(sysDate.getDate());
}