function dataroomFileUploadCheck(f) {
	var titleField = f.title;
	var fileField = f.fileTemp;
	if (isEmpty(titleField)) {
		alert("파일 제목을 입력하세요");
		titleField.focus();
		return false;
	}

	if (isEmpty(fileField)) {
		alert("파일이 없습니다");
		fileField.focus();
		return false;
	}
	
	return true;
}

/*
function galleryImgUploadCheck(f) {
   var titleField = f.title;
   var fileField = f.fileTemp;
   if (isEmpty(titleField) || isEmpty(fileField) || (isNotType(fileField, "png") && isNotType(fileField, "jpg") && isNotType(fileField, "gif"))) {
	  alert("?");
	  titleField.focus();
	  return false;
   }
   return true;
}
*/
function joinCheck(f) {
	var idField = f.email; var pwField = f.pw;
	var pwChkField = f.pwChk; var nameField = f.name;
	var addr1Field = f.addr1; var addr2Field = f.addr2;
	var addr3Field = f.addr3; var photoField = f.photoTemp;
	var telField = f.tel; var classIDField = f.classId;

	var classPwField = f.classPw;
	if (isEmpty(idField) || containsHS(idField)
		|| !idField.value.includes("@") || !idField.value.includes(".")) {
		alert("ID 형식을 올바르게 작성해주세요(이메일)");
		idField.value = "";
		idField.focus();
		return false;
	}

	if (isEmpty(nameField)) {
		alert("이름을 입력하세요");
		nameField.value = "";
		nameField.focus();
		return false;
	}

	if (isEmpty(pwField) || notEqual(pwField, pwChkField)
		|| notContains(pwField, "1234567890")) {
		alert("비밀번호를 확인해주세요");
		pwField.value = "";
		pwChkField.value = "";
		pwField.focus();
		return false;
	}
	if (isEmpty(telField)) {
		alert("전화번호를 입력하세요");
		telField.value = "";
		telField.focus();
		return false;
	}
	if (isEmpty(addr1Field) || isEmpty(addr2Field) || isEmpty(addr3Field)) {
		alert("주소를 입력하세요");
		addr1Field.value = "";
		addr2Field.value = "";
		addr3Field.value = "";
		return false;
	}
	if (isEmpty(photoField) || (isNotType(photoField, "png") && isNotType(photoField, "gif") && isNotType(photoField, "jpg"))) {
		alert("프로필 사진을 입력해주세요(png, gif, jpg)");
		photoField.value = "";
		return false;
	}
	if (isEmpty(classIDField)) {
		alert("반 번호를 입력해주세요");
		classIDField.value = "";
		classIDFieldField.focus();
		return false;
	}
	if (isEmpty(classPwField)) {
		alert("반 비밀번호를 입력해주세요");
		classPwField.value = "";
		classPwField.focus();
		return false;
	}
	
	if ($("#classPw").css("color") == "rgb(255, 0, 0)") {
			alert("반 비밀번호를 다시 입력해주세요");
			classPwField.value = "";
			classIDField.focus();
			return false;
		}
	alert("회원 가입 성공");
	return true;
}

function loginCheck(f) {
	var idField = f.email; var pwField = f.pw;
	var loginCheckField = f.loginValidCheck
	if (isEmpty(idField) || isEmpty(pwField)) {
		alert("ID(이메일) 혹은 비밀번호를 입력하세요");
		idField.value = "";
		pwField.value = "";
		idField.focus();
		return false;
	}

	if (loginCheckField.value == "0") {
		alert("ID(이메일) 혹은 비밀번호를 확인하세요");
		idField.value = "";
		pwField.value = "";
		idField.focus();
		return false;
	}
	alert("로그인 성공");
	return true;
}
function userUpdateCheck(f) {
	var pwField = f.pw; var pwChkField = f.pwChk;
	var nameField = f.name; var addr1Field = f.addr1;
	var addr2Field = f.addr2; var addr3Field = f.addr3;
	var photoField = f.photoTemp; var telField = f.tel;

	if (isEmpty(pwField) || notEqual(pwField, pwChkField)
		|| notContains(pwField, "1234567890")) {
		alert("비밀번호를 확인해주세요");
		pwField.value = "";
		pwChkField.value = "";
		pwField.focus();
		return false;
	}
	if (isEmpty(nameField)) {
		alert("이름을 입력하세요");
		nameField.value = "";
		nameField.focus();
		return false;
	}
	if (isEmpty(telField)) {
		alert("전화번호를 입력하세요");
		telField.value = "";
		telField.focus();
		return false;
	}
	if (isEmpty(addr1Field) || isEmpty(addr2Field) || isEmpty(addr3Field)) {
		alert("주소를 입력하세요");
		addr1Field.value = "";
		addr2Field.value = "";
		addr3Field.value = "";
		return false;
	}
	if (isEmpty(photoField)) {
		return true;
	}
	if (isNotType(photoField, "png")
		&& isNotType(photoField, "gif")
		&& isNotType(photoField, "jpg")) {
		alert("프로필 사진은 png, gif, jpg만 업로드 가능합니다");
		photoField.value = "";
		return false;
	}
	alert("수정 완료되었습니다.")
	return true;
}
function boardWriteCheck(f) {
	var titleField = f.title;
	var contentField = f.content;
	var photoField = f.boardImageFile;



	if (isEmpty(titleField)) {
		alert("제목을 입력하세요");
		titleField.value = "";
		titleField.focus();
		return false;
	}
	if (isEmpty(contentField)) {
		alert("내용을 입력하세요");
		contentField.value = "";
		contentField.focus();
		return false;
	}
	if (!isEmpty(photoField) && (isNotType(photoField, "png") && isNotType(photoField, "gif") && isNotType(photoField, "jpg"))) {
		alert("이미지는 png, gif, jpg만 업로드 가능합니다");
		photoField.value = "";
		return false;
	}

	return true;
}

function noticeWriteCheck(f) {
	var titleField = f.noticeTitle;
	var contentField = f.noticeContent;
	var photoField = f.noticeImageFile;



	if (isEmpty(titleField)) {
		alert("제목을 입력하세요");
		titleField.value = "";
		titleField.focus();
		return false;
	}
	if (isEmpty(contentField)) {
		alert("내용을 입려하세요");
		contentField.value = "";
		contentField.focus();
		return false;
	}
	if (!isEmpty(photoField) && (isNotType(photoField, "png") && isNotType(photoField, "gif") && isNotType(photoField, "jpg"))) {
		alert("이미지는 png, gif, jpg만 업로드 가능합니다");
		photoField.value = "";
		return false;
	}

	return true;
}


function noticeUpdateWriteCheck(f) {
	var titleField = f.noticeTitle;
	var contentField = f.noticeContent;
	var photoField = f.noticeImageFile;



	if (isEmpty(titleField)) {
		alert("제목을 입력하세요");
		titleField.value = "";
		titleField.focus();
		return false;
	}
	if (isEmpty(contentField)) {
		alert("내용을 입력하세요");
		contentField.value = "";
		contentField.focus();
		return false;
	}
	if (!isEmpty(photoField) && (isNotType(photoField, "png") && isNotType(photoField, "gif") && isNotType(photoField, "jpg"))) {
		alert("이미지는 png, gif, jpg만 업로드 가능합니다");
		photoField.value = "";
		return false;
	}

	return true;
}
function classIdCheck(f) {
	var classIdField = f.classId;
	var classPwField = f.classPw;
	var calendarField = f.classCalendarId;



	if (isEmpty(classIdField)) {
		alert("반 번호를 입력하세요");
		classIdField.value = "";
		classIdField.focus();
		return false;
	}
	if (isEmpty(classPwField)) {
		alert("반 비밀번호를 입력하세요");
		classPwField.value = "";
		classPwField.focus();
		return false;
	}

	if (isEmpty(calendarField)) {
		alert("캘린더 주소를 입력하세요");
		calendarField.value = "";
		calendarField.focus();
		return false;
	}
	return true;
}


function findEmailCheck(f) {
	var nameField = f.name; var telField = f.tel;


	if (isEmpty(nameField)) {
		alert("이름을 입력하세요");
		nameField.value = "";
		nameField.focus();
		return false;
	}
	if (isEmpty(telField)) {
		alert("전화번호를 입력하세요");
		telField.value = "";
		telField.focus();
		return false;
	}
	return true;
}



function findPwCheck(f) {
	var nameField = f.name; var telField = f.tel;


	if (isEmpty(nameField)) {
		alert("이름을 입려하세요");
		nameField.value = "";
		nameField.focus();
		return false;
	}
	if (isEmpty(telField)) {
		alert("전화번호를 입력하세요");
		telField.value = "";
		telField.focus();
		return false;
	}
	return true;
}

function boardUpdateWriteCheck(f) {
	var titleField = f.title;
	var contentField = f.content;
	var photoField = f.boardImageFile;

	if (isEmpty(titleField)) {
		alert("제목을 입력하세요");
		titleField.value = "";
		titleField.focus();
		return false;
	}
	if (isEmpty(contentField)) {
		alert("내용을 입력하세요");
		contentField.value = "";
		contentField.focus();
		return false;
	}
	if (!isEmpty(photoField) && (isNotType(photoField, "png") && isNotType(photoField, "gif") && isNotType(photoField, "jpg"))) {
		alert("이미지는 png, gif, jpg만 업로드 가능합니다");
		photoField.value = "";
		return false;
	}

	return true;
}

function commentsWriteCheck(f) {
	var contentField = f.content;

	if (isEmpty(contentField)) {
		alert("댓글을 입력하세요");
		contentField.value = "";
		contentField.focus();
		return false;
	}

	return true;
}

function commentsUpdateWriteCheck(f) {
	var contentField = f.content;

	if (isEmpty(contentField)) {
		alert("대댓글을 입력하세요");
		contentField.value = "";
		contentField.focus();
		return false;
	}

	return true;
}


function choicesCheck(f) {
	var inputElements = document.getElementsByName("choices");
	var isChecked = false;
	for (var i = 0; i < inputElements.length; i++) {
		if (inputElements[i].checked) {
			isChecked = true;
			break;
		}
	}
	if (!isChecked) {
		alert("투표 항목이 선택되지 않았습니다");
		inputElements[0].focus();
		return false;
	}
	return true;
}
