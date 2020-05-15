$(document).ready(function () {
    let strTabFlag = $("#strTabFlag").val();
    if ("" == strTabFlag) {
        strTabFlag = "tab1";
    }
    selectItem(strTabFlag);
})

function selectItem(strTabFlag) {
    changeTab(strTabFlag)
    $("#strTabFlag").val(strTabFlag);
}