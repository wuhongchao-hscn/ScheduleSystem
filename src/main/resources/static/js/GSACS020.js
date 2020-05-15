$(document).ready(function () {
    let arrayObj = $("#deleteUserIdHd").val().split(",");
    let arrIndex = 0;
    let arrlength = arrayObj.length;
    if (arrlength > 0) {
        $('input[type="checkbox"][name="deleteFlg"]').each(
            function () {
                if ($(this).val() == arrayObj[arrIndex]) {
                    $(this).prop("checked", true);
                    arrIndex++;
                    if (arrIndex >= arrlength) {
                        return true;
                    }
                }
            }
        );
    }
})

function getdeleteUserId() {
    let arrayObj = new Array();
    $('input[type="checkbox"][name="deleteFlg"]:checked').each(
        function () {
            arrayObj.push($(this).val());
        }
    );
    $("#deleteUserIdHd").val(arrayObj.join(","));
}