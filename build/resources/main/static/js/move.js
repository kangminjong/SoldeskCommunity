function logoutDo() {
	location.href = "logout.do"
}
function infoGo() {
	location.href = "info.go"
}
function writeGo() {
	location.href = "write.go"
}
function userInfoGo() {
	location.href = "user.info.go";
}

function byeDo() {
	if (prompt("탈퇴하려면 탈퇴 입력") == "탈퇴") {
		location.href = "bye.do";
	}
}

function boardPostReplyDelete(commentId) {
	if (confirm("삭제하시겠습니까?")) {
		// URL에 commentId가 포함되어 있는지 확인
		console.log("삭제 요청 commentId:", commentId);  // commentId가 콘솔에 제대로 출력되는지 확인
		location.href = "board.post.reply.delete?commentId=" + commentId;
	}
}
function cocomentsInput() {
	$(document).on("click", ".cocomentsBtn", function() {
		$(this).hide(); // 클릭한 버튼 숨기기
		$(this).closest(".aSNSPostReply").find(".replyInput").first().css("display", "block");
	});

	$(document).on("click", ".replySubmit", function() {
		let replyText = $(this).siblings(".boardcocoments2Input").val();
		if (replyText.trim() !== "") {
			$(this).closest(".replyInput").css("display", "none");
			$(this).closest(".aSNSPostReply").find(".cocomentsBtn").first().css("display", "inline-block");
		}
	});
}
function boardPostReply22Delete(cocommentId) {
	if (confirm("삭제하시겠습니까??")) {
		// URL에 commentId가 포함되어 있는지 확인
		console.log("삭제 요청 cocommentId:", cocommentId);  // commentId가 콘솔에 제대로 출력되는지 확인
		location.href = "board.post.reply22.delete?cocommentId=" + cocommentId;
	}
}
function writeGo() {
	location.href = "write.go";
}

function snsPageChange(page) {
	location.href = "sns.page.change?page=" + page;
}
function boardDelete(boardID) {
	location.href = "board.delete?boardID=" + boardID;
}
function dataroomFileDelete(no) {
	if (confirm("삭제하시겠습니까??")) {
		location.href = "dataroom.file.delete?no=" + no;
	}
}

function dataroomFileDownload(name) {
	location.href = "dataroom/" + name;
}

function noticeWriteGo() {
	location.href = "/notice.write.go";
}

function noticeDelete(noticeID) {
	location.href = "notice.delete?noticeID=" + noticeID;
}

function classIdDelete(classId) {
	if (confirm("정말 삭제하시겠습니까??")) {
		location.href = "admin.classId.delete?classId=" + classId;
	}
}

function noticePageChange(page) {
	location.href = "notice.page.change?page=" + page;
}

function writeShow(boardID) {
	location.href = "write.show?boardID=" + boardID;
}

function noticeWriteShow(noticeID) {
	location.href = "notice.write.show?noticeID=" + noticeID;
}

function voteDelete(voteId) {
	location.href = "/vote/votice.delete?voteId=" + voteId;
}

function adminNoticeDelete(noticeID) {
	if (confirm("정말 삭제하시겠습니까??")) {
		location.href = "admin.notice.delete?noticeID=" + noticeID;
	}
}