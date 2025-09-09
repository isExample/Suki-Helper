// ===== 한글 라벨 맵 =====
const PLACE_LABELS = {
    SCHOOL: '학교',
    HOME: '집',
    PARK: '공원',
    CAFE: '카페',
    LIBRARY: '도서관',
    GOLD_MINE: '금광',
    ART_GALLERY: '미술관',
    GYM: '헬스장',
    PC_ROOM: '피시방',
    FOOTBALL_PITCH: '축구연습장',
    WORKSHOP: '작업실',
    PRACTICE_ROOM: '연습실'
};

const ACTION_LABELS = {
    STUDY: '공부',
    PART_TIME: '알바',
    EXERCISE: '운동',
    SLEEP: '잠자기',
    ATTEND_CLASS: '수업듣기',
    PLAY_GAME: '게임연습',
    FOOTBALL: '축구연습',
    DRAWING: '그림연습',
    TRAINING: '아이돌연습'
};

const placeLabel  = v => PLACE_LABELS[v]  ?? v;
const actionLabel = v => ACTION_LABELS[v] ?? v;

(() => {
    const $ = (sel, root = document) => root.querySelector(sel);
    const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));

    const ctx = $('meta[name="ctx"]')?.content || '';

    function pickList(name) {
        return $$(`input[name="${name}"]:checked`).map(el => el.value);
    }

    function buildConsumables() {
        const ids = ['DECAF_COFFEE', 'PUFFED_RICE', 'ROYAL_FEAST', 'CHOCOLATE_MILK', 'WHITE_MILK', 'COFFEE', 'COFFEE_X2'];
        const map = {};
        ids.forEach(k => {
            const el = $(`#qty-${k}`);
            const n = Number(el?.value || 0);
            if (n >= 1) map[k] = n;
        });
        return map;
    }

    function buildRequest() {
        return {
            targetStamina: Number($('#targetStamina')?.value || 0),
            fitnessLevel: Number($('#fitnessLevel')?.value || 0),
            day: $('input[name="day"]:checked')?.value || 'WEEKDAY_OTHER',
            inactiveList: pickList('inactiveList'),
            activeList: pickList('activeList'),
            badgeList: pickList('badgeList'),
            traitList: pickList('traitList'),
            permanentItemList: pickList('permanentItemList'),
            consumableItemMap: buildConsumables()
        };
    }

    function validateClient(payload) {
        // 커피 제약 검증
        const c1 = payload.consumableItemMap['COFFEE'] || 0;
        const c2 = payload.consumableItemMap['COFFEE_X2'] || 0;
        if (c1 > 1) throw new Error('커피는 1개를 초과할 수 없습니다.');
        if (c1 > 0 && c2 > 0) throw new Error('커피와 커피x2는 동시에 사용할 수 없습니다.');
    }

    // ===== 결과 렌더링 =====
    const resultSec = $('#result');
    const emptyP = $('#result-empty');
    const resultWrap = $('#result-wrap');
    const countSpan = $('#r-count');
    const comboListEl = $('#combo-list');

    function renderTicks(ticks){
        return ticks.map(t => {
            const cls = t.action === 'SLEEP' ? 'tick tick--sleep' : 'tick tick--other';
            return `
              <div class="${cls}">
                <span class="action">${actionLabel(t.action)}</span>
                <span class="stamina">${t.stamina}</span>
              </div>
            `;
        }).join('');
    }

    function renderCombo(combo, idx) {
        // 조합은 장소1(6틱) + 장소2(8틱) 고정
        const p1 = combo.slice(0, 6);
        const p2 = combo.slice(6, 14);

        const p1Name = p1[0]?.place ? placeLabel(p1[0].place) : '장소1';
        const p2Name = p2[0]?.place ? placeLabel(p2[0].place) : '장소2';

        // 장소2가 없으면 블록 자체를 생략
        const place2Html = p2.length ? `
            <div class="place">
              <div class="place-title">${p2Name} (8틱)</div>
              <div class="ticks">${renderTicks(p2)}</div>
            </div>
          ` : '';

        return `
        <li>
          <div class="combo">
            <div class="combo-head">
              <span class="title">조합 #${idx + 1}</span>
              <span class="info">총 ${combo.length}틱</span>
            </div>
            <div class="place-grid">
              <div class="place">
                <div class="place-title">${p1Name} (6틱)</div>
                <div class="ticks">${renderTicks(p1)}</div>
              </div>
              ${place2Html}
            </div>
          </div>
        </li>
      `;
    }

    function renderResult(resp) {
        // SimulationResponse
        resultSec.hidden = false;

        if (!resp || !Array.isArray(resp.combinations) || resp.count === 0) {
            emptyP.hidden = false;
            resultWrap.hidden = true;
            return;
        }

        emptyP.hidden = true;
        resultWrap.hidden = false;

        countSpan.textContent = String(resp.count);
        comboListEl.innerHTML = resp.combinations.map(renderCombo).join('');
    }

    // ===== 버튼 클릭 -> API 호출 =====
    const btn = $('#buildRequestBtn');
    if (!btn) return;

    btn.addEventListener('click', async () => {
        try {
            const payload = buildRequest();
            validateClient(payload);

            const headers = {'Content-Type': 'application/json'};

            btn.disabled = true;
            btn.textContent = '요청 중...';

            const res = await fetch(`${ctx}/api/v1/simulations/reach`, {
                method: 'POST',
                headers,
                body: JSON.stringify(payload),
            });

            const text = await res.text();
            const parsed = (() => {
                try {
                    return JSON.parse(text);
                } catch {
                    return null;
                }
            })();
            const data = parsed && typeof parsed === 'object' ? (parsed.data ?? parsed) : parsed ?? text;

            // 다른 스크립트/결과 UI에서 사용할 수 있도록 이벤트 발행
            document.dispatchEvent(new CustomEvent('reach:response', {detail: {ok: res.ok, data}}));

            if (!res.ok) throw new Error(typeof data === 'string' ? data : '요청 실패');

            renderResult(data);

        } catch (err) {
            console.error('[Reach ERROR]', err);
            alert(err?.message || '요청 중 오류가 발생했습니다.');
            document.dispatchEvent(new CustomEvent('reach:error', {detail: err}));
        } finally {
            btn.disabled = false;
            btn.textContent = '조합 조회';
        }
    });
})();
