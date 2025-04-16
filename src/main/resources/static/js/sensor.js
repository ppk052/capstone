let plantId = 0;

// "옵션 추가" 선택 시 모달 창 띄우기
function checkAddOption() {
    let select = document.getElementById("deviceOnOff");
    if (select.value === "add-option") {
        let modal = new bootstrap.Modal(document.getElementById("addOptionModal"));
        modal.show();
        select.value = ""; // 기본 선택 상태로 변경
    }
}

// 새 옵션 추가 및 localStorage 저장
function addDropdownOption() {
    let select = document.getElementById("deviceOnOff");
    let text = document.getElementById("newOptionText").value.trim();
    let value = document.getElementById("newOptionValue").value.trim();

    if (!text || !value) {
        alert("옵션 값과 이름을 입력하세요!");
        return;
    }

    let option = document.createElement("option");
    option.value = value;
    option.text = text;
    select.insertBefore(option, select.lastElementChild); // "옵션 추가" 앞에 삽입

    saveOptionToLocal(text, value); // 옵션을 localStorage에 저장

    // 입력 필드 초기화 및 모달 닫기
    document.getElementById("newOptionText").value = "";
    document.getElementById("newOptionValue").value = "";
    let modal = bootstrap.Modal.getInstance(document.getElementById("addOptionModal"));
    modal.hide();
}

// localStorage에 옵션 저장
function saveOptionToLocal(text, value) {
    let options = JSON.parse(localStorage.getItem("dropdownOptions")) || [];
    options.push({ text, value });
    localStorage.setItem("dropdownOptions", JSON.stringify(options));
}

// localStorage에서 옵션 불러오기
function loadOptions() {
    let options = JSON.parse(localStorage.getItem("dropdownOptions")) || [];
    let select = document.getElementById("deviceOnOff");

    options.forEach(({ text, value }) => {
        let option = document.createElement("option");
        option.value = value;
        option.text = text;
        select.insertBefore(option, select.lastElementChild);
    });
}

function fetchSensorData(plantId) {
    const LEDModeBtn = document.getElementById('LEDModeBtn');
    const pumpModeBtn = document.getElementById('pumpModeBtn');
    const fanModeBtn = document.getElementById('fanModeBtn');
    const LEDBtn = document.getElementById("LEDBtn");
    const pumpBtn = document.getElementById("pumpBtn");
    const fanBtn = document.getElementById("fanBtn");
    fetch(`/api/sensor/${plantId}`)
        .then(response => response.json())
        .then(data => {
            document.getElementById("landMoisture").innerText = data.landMoisture + "%";
            document.getElementById("temperature").innerText = data.temperature + "°C";
            document.getElementById("light").innerText = data.light + "lx";
            document.getElementById("landMoistureAppropriate").innerText = data.landMoistureAppropriate + "%";
            document.getElementById("temperatureAppropriate").innerText = data.temperatureAppropriate + "°C";
            document.getElementById("lightAppropriate").innerText = data.lightAppropriate + "lx";
            if(data.LEDAuto) {
                LEDModeBtn.innerText = '자동';
            } else {
                LEDModeBtn.innerText = '수동';
            }
            LEDModeBtn.classList.toggle("auto",data.LEDAuto);
            LEDModeBtn.classList.toggle("manual",!data.LEDAuto);
            if(data.pumpAuto) {
                pumpModeBtn.innerText = '자동';
            } else {
                pumpModeBtn.innerText = '수동';
            }
            pumpModeBtn.classList.toggle("auto",data.pumpAuto);
            pumpModeBtn.classList.toggle("manual",!data.pumpAuto);
            if(data.fanAuto) {
                fanModeBtn.innerText = '자동';
            } else {
                fanModeBtn.innerText = '수동';
            }
            fanModeBtn.classList.toggle("auto",data.fanAuto);
            fanModeBtn.classList.toggle("manual",!data.fanAuto);
            if(data.LED) {
                LEDBtn.innerText = 'on';
            } else {
                LEDBtn.innerText = 'off';
            }
            LEDBtn.classList.toggle("on",data.LED);
            LEDBtn.classList.toggle("off",data.LED);
            if(data.pump) {
                pumpBtn.innerText = 'on';
            } else {
                pumpBtn.innerText = 'off';
            }
            pumpBtn.classList.toggle("on",data.pump);
            pumpBtn.classList.toggle("off",!data.pump);
            if(data.fan) {
                fanBtn.innerText = 'on';
            } else {
                fanBtn.innerText = 'off';
            }
            fanBtn.classList.toggle("on",data.fan);
            fanBtn.classList.toggle("off",!data.fan);
        })
        .catch(error => console.error(error));
}

//자동수동버튼
function toggleMode(device) {
    const modeBtn = document.getElementById(device + 'ModeBtn');
    const currentState = modeBtn.classList.contains("auto");
    const newState = !currentState;

    // 서버로 상태 변경 요청 보내기
    fetch(`/api/device/auto`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ plantId: plantId, device: device , state: newState })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("서버 오류");
            }
            return response.text().then(text => text ? JSON.parse(text) : null);
        })
        .then(data => {
            // UI 업데이트
            modeBtn.innerText = newState ? "자동" : "수동"; // 글자 변경
            modeBtn.classList.toggle("auto", newState);
            modeBtn.classList.toggle("manual", !newState);
        })
        .catch(error => console.error("장치 상태 변경 실패:", error));
}

//장치 온오프
function toggleDevice(device) {
    const btn = document.getElementById(device + "Btn"); // 버튼 요소 찾기
    const currentState = btn.classList.contains("on"); // 현재 상태 (ON인지 확인)
    const autoState = document.getElementById(device + 'ModeBtn').classList.contains('auto');

    if(autoState) {
        alert('먼저 자동모드를 수동모드로 변경해주세요');
        return;
    }

    // 새로운 상태 결정 (ON → OFF, OFF → ON)
    const newState = !currentState;

    // 서버로 상태 변경 요청 보내기
    fetch(`/api/device`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ plantId: plantId, device: device , state: newState })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("서버 오류");
            }
            return response.text().then(text => text ? JSON.parse(text) : null);
        })
        .then(data => {
            // UI 업데이트
            btn.innerText = newState ? "ON" : "OFF"; // 글자 변경
            btn.classList.toggle("on", newState);
            btn.classList.toggle("off", !newState);
        })
        .catch(error => console.error("장치 상태 변경 실패:", error));
}
document.addEventListener("DOMContentLoaded", loadOptions);
document.getElementById("deviceOnOff").addEventListener("change", function () {
    plantId = this.value;
    fetchSensorData(plantId);
})
setInterval(() => fetchSensorData(plantId), 3000);
fetchSensorData(0);