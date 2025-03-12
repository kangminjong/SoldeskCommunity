 /**
 * 한글처리x
 * 1) 최종적으로는 utf-8 쓸거고
 * 2) 한글쓸일없음
 */

// 라이브러리 : 어떤 웹사이트에서든 쓸수있게 최대한 일반적으로
// 부정적으로(잘못됐으면 true)

// input넣었을때
// 입력한 내용이 없으면 true
function isEmpty(input) {
	return !input.value;
}  

// input, 글자수
// 그거보다 짧으면 true
function lessThan(input, len) {
	return input.value.length < len;
}

// input
// 한글/한자/특수문자 있으면 true
function containsHS(input) {
	var set = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890@-_.";
	for (var i = 0; i < input.value.length; i++) {
		if (set.indexOf(input.value[i]) == -1) {
			return true;
		}
	}
}

// input x 2
// 내용 다르면 true
function notEqual(input1, input2) {
	return input1.value != input2.value;
}

// input, 문자열세트 넣어서
// 문자열세트중에 하나도 없으면 true
function notContains(input, set){
	for (var i = 0; i < set.length; i++){
		if (input.value.indexOf(set[i]) != -1) {
			return false;
		}
	}
	return true;
}

// input
// 숫자 아닌게 있으면 true
function isNotNum(input) {
	return (isNaN(input.value) || input.value.indexOf(" ") != -1);	
}

// input, 확장자
// 그 파일 아니면 true
function isNotType(input, type) {
	type = "." + type;
	return input.value.toLowerCase().indexOf(type) == -1;
}








