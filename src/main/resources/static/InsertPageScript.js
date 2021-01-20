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
                var busStaion = "<li><span>" + json.results[i].stationNm + "(" + json.results[i].stationId + ")" +"</span><input type='button' class='selectBtn' value='도착정보 적재' onclick='insertArrivalInfo("+ json.results[i].stationId + ");'/></li>";
                busStationList.append(busStaion);
            }
        })
}

//도착정보 적재API 호출
function insertArrivalInfo(stationId){

    $("#apicall").find('span').text('');
    $("#elastic").find('span').text('');
    $("#redis").find('span').text('');
    $("#kafka").find('span').text('');
    $("#mongo").find('span').text('');
    $("span").parent().css({backgroundColor:"#ffffff"});

    $.ajax({
        url: '/andongbus/insertArrivalinfo',  // 클라이언트가 요청을 보낼 서버의 URL 주소
        data: {                                                                  // HTTP 요청과 함께 서버로 보낼 데이터
            stationId: stationId
        },
        type: "GET",                                                            // HTTP 요청 방식(GET, POST)
        dataType: "json"                                                        // 서버에서 보내줄 데이터의 타입
    })
        .done(function(json) {
            $("#apicall").find('span').text(json.apiCall);
            $("#elastic").find('span').text(json.elasticSearch);
            $("#redis").find('span').text(json.redis);
            $("#kafka").find('span').text(json.kafka);
            $("#mongo").find('span').text(json.mongo);
            $("span:contains('SUCCESS')").parent().css({backgroundColor:"#ebfaeb"});
            $("span:contains('FAIL')").parent().css({backgroundColor:"#faefe9"});
            $("span:contains('STOPED')").parent().css({backgroundColor:"#ececec"});
            $("span:contains('NOT')").parent().css({backgroundColor:"#ececec"});
        })
}

function errorListPaging(){
    var busStationList = $("#busStationList");
    busStationList.empty();

    //새로운 데이터 리스트를 뿌려준다.
    var i;
    var busStaion = "<li><span>에러 1 (STATIONID IS NULL)</span><input type='button' class='selectBtn' value='도착정보 적재' onclick='insertArrivalInfo();'/></li>";
    busStationList.append(busStaion);

    var busStaion = "<li><span>에러 2 (STATIONID IS NOT VALID)</span><input type='button' class='selectBtn' value='도착정보 적재' onclick='insertArrivalInfo(123);'/></li>";
    busStationList.append(busStaion);

}