function connectMemberAddrInputEvent() {
	$("#memberAddr1Input, #memberAddr2Input").click(function() {
		new daum.Postcode({
			oncomplete: function(data) {
				$("#memberAddr1Input").val(data.zonecode);
				$("#memberAddr2Input").val(data.roadAddress);
			}
		}).open();
	});
}


function isNameDuplicate(playerName) {
	let scores = JSON.parse(localStorage.getItem("scores")) || [];
	return scores.some(score => score.name === playerName);
}

function classIdtoJsonchk() {
	$("#classPw").keyup(function(e) {
		var classId = $("#classId").val();
		var classPw = $(this).val();
		$.getJSON("/classId.get?classId=" + classId + "&classPw=" + classPw, function(classIdData) {

			if (classIdData.classId[0] != null) {
				console.log("여기");
				$("#classId").css("color", "black");
				$("#classPw").css("color", "black");
			} else {
				$("#classId").css("color", "red");
				$("#classPw").css("color", "red");
			}
		})
	});
}


function userEmailtoJsonchk() {
	$(".emailPw").keyup(function() {
		var email = $(".loginEmail").val();
		var pw = $(this).val();
		$.getJSON("/UserEmail.get?email=" + email + "&pw=" + pw, function(UserData) {
			if (UserData.user[0] != null) {
				$("#loginValidCheck").val(1);
			} else {
				$("#loginValidCheck").val(0);
			}
		})
	});
}

$(function() {
	connectMemberAddrInputEvent();
	classIdtoJsonchk();
	userEmailtoJsonchk();
});
