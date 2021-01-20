//페이징 버튼 추가
window.onload = function() {
    $.ajax({
        url: '/andongbus/stationListPaging',  // 클라이언트가 요청을 보낼 서버의 URL 주소
        data: {                                                                  // HTTP 요청과 함께 서버로 보낼 데이터
            type: "Paging",
            pageSize: 10,
            pageUnit: 10,
            pageIndex: 1,
        },
        type: "GET",                                                            // HTTP 요청 방식(GET, POST)
        dataType: "json"                                                        // 서버에서 보내줄 데이터의 타입
    })
        .done(function(json) {
            var pagingDiv = $("#pagingDiv");
            //페이징 버튼을 뿌려준다.
            var i;
            var lastIndex = json.parameter.lastIndex;
            for(i=1; i<=lastIndex; i++){
                var pageBtn = "<input class='pageBtn' type='button' value='" + i + "' onClick='busStationListPaging(" + i + ");'>";
                pagingDiv.append(pageBtn);
            }
        })

    busStationListPaging(1);
}
//버스정류장 목록 호출
function busStationListPaging(page){
    $.ajax({
        url: '/andongbus/stationListPaging',  // 클라이언트가 요청을 보낼 서버의 URL 주소
        data: {                                                                  // HTTP 요청과 함께 서버로 보낼 데이터
            pageIndex: page
        },
        type: "GET",                                                            // HTTP 요청 방식(GET, POST)
        dataType: "json"                                                        // 서버에서 보내줄 데이터의 타입
    })
        .done(function(json) {
            var busStationList = $("#busStationList");

            //존재하는 리스트를 지워준다.
            busStationList.empty();

            //새로운 데이터 리스트를 뿌려준다.
            var i;
            var pageunit = json.parameter.pageUnit;
            for(i=0; i<pageunit; i++){
                var busStaion = "<li><span>" + json.results[i].stationNm + "(" + json.results[i].stationId + ")" +"</span><input type='button' class='selectBtn' value='버스도착 조회' onclick='selectArrivalInfo("+ json.results[i].stationId + ");'/></li>";
                busStationList.append(busStaion);
            }
        })
}

//도착정보 조회API 호출
function selectArrivalInfo(stationId){
    var busList = $("#busList");
    busList.empty();
    $.ajax({
        url: '/andongbus/selectArrivalinfo',  // 클라이언트가 요청을 보낼 서버의 URL 주소
        data: {                                                                  // HTTP 요청과 함께 서버로 보낼 데이터
            stationId: stationId
        },
        type: "GET",                                                            // HTTP 요청 방식(GET, POST)
        dataType: "json"                                                        // 서버에서 보내줄 데이터의 타입
    })
        .done(function(json) {
            //버스도착 리스트를 뿌려준다.
            var i;
            var infoLength = json.length;
            if(infoLength <= 0){
                busList.text("조회 데이터가 없습니다.");
            }
            var wrapBusPrd;
            for(i=0; i<infoLength; i++){
                var divFirst = "<div>";
                var wrapBusNm = "<div class='wrapBusNm'>"+ json[i].routeNum + "</div>";
                var wrapBusVia = "<div class='wrapBusVia'>" + json[i].via + "</div>";
                if(json[i].predictTm == null || json[i].predictTm == ''){
                    wrapBusPrd = "<div class='wrapBusPrd'><span>정보없음</span></div>";
                }else{
                    wrapBusPrd = "<div class='wrapBusPrd'><span>" + json[i].predictTm + "분 후 도착예정</span><span>(" + json[i].remainStation + "번째전)</span></div>";
                }
                var divLast = "</div>";
                var arrivalInfo = divFirst.concat(wrapBusNm).concat(wrapBusVia).concat(wrapBusPrd).concat(divLast);
                busList.append(arrivalInfo);
            }
        })
}

