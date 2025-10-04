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

const ITEM_LABELS = {
    DECAF_COFFEE: { name: '디카페인 커피', effect: '+15'},
    PUFFED_RICE: { name: '뻥튀기', effect: 'x2'},
    ROYAL_FEAST: { name: '수라상', effect: '+100'},
    CHOCOLATE_MILK: { name: '초코우유', effect: '+50'},
    WHITE_MILK: { name: '흰 우유', effect: '+30'},
    COFFEE: { name: '커피', effect: '+25'},
    COFFEE_X2: { name: '커피 x2', effect: '+50'}
};

const placeLabel = v => PLACE_LABELS[v] ?? v;
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

    function buildRequest(root = document) {
        const min = root.querySelector('#targetMin');
        const max = root.querySelector('#targetMax');

        const base = {
            fitnessLevel: Number($('#fitnessLevel', root)?.value || 0),
            day: $('input[name="day"]:checked', root)?.value || 'WEEKDAY_OTHER',
            inactiveList: pickList('inactiveList', root),
            activeList: pickList('activeList', root),
            badgeList: pickList('badgeList', root),
            traitList: pickList('traitList', root),
            permanentItemList: pickList('permanentItemList', root),
            consumableItemMap: buildConsumables(root)
        };

        if (min && max) {
            // range 모드
            return {
                ...base,
                targetMin: Number(min.value || 0),
                targetMax: Number(max.value || 0)
            };
        } else {
            // single 모드
            return {
                ...base,
                targetStamina: Number($('#targetStamina', root)?.value || 0)
            };
        }
    }

    function validateClient(payload) {
        // 커피 제약 검증
        const c1 = payload.consumableItemMap['COFFEE'] || 0;
        const c2 = payload.consumableItemMap['COFFEE_X2'] || 0;
        if (c1 > 1) throw new Error('커피는 1개를 초과할 수 없습니다.');
        if (c2 > 1) throw new Error('커피x2는 1개를 초과할 수 없습니다.');
        if (c1 > 0 && c2 > 0) throw new Error('커피와 커피x2는 동시에 사용할 수 없습니다.');

        // 범위형 target 검증
        if (typeof payload.targetMin === 'number' && typeof payload.targetMax === 'number') {
            const n1 = payload.targetMin, n2 = payload.targetMax;
            if (n1 < 1 || n1 > 99 || n2 < 1 || n2 > 99) throw new Error('목표 체력 범위는 1~99입니다.');
            if (n1 >= n2) throw new Error('N1은 N2보다 작아야 합니다.');
        } else if (typeof payload.targetStamina === 'number') {
            const n = payload.targetStamina;
            if (n < 1 || n > 99) throw new Error('목표 체력은 1~99입니다.');
        }

        // 특성 최대 6개 검증
        if (Array.isArray(payload.traitList) && payload.traitList.length > 6) {
            throw new Error('특성은 최대 6개까지 선택 가능합니다.');
        }
    }

    // ===== 결과 렌더링 =====
    function renderTicks(ticks) {
        return ticks.map(t => {
            const cls = t.action === 'SLEEP' ? 'tick tick--sleep' : 'tick tick--other';

            const itemHtml = (t.item && ITEM_LABELS[t.item])
                ? `
                    <div class="tick tick--item">
                      <span class="item-name">${ITEM_LABELS[t.item].name}</span>
                      <span class="item-effect">${ITEM_LABELS[t.item].effect}</span>
                    </div>
              `
                : '';

            return `
              <div class="tick-wrap">
                <div class="${cls}">
                  <span class="action">${actionLabel(t.action)}</span>
                  <span class="stamina">${t.stamina}</span>
                </div>
                ${itemHtml}
              </div>
            `;
        }).join('');
    }

    function renderCombo(combo, idx) {
        const p1 = combo.slice(0, 6);
        const p2 = combo.slice(6, 14);
        const p1Name = p1[0]?.place ? placeLabel(p1[0].place) : '장소1';
        const p2Name = p2[0]?.place ? placeLabel(p2[0].place) : '장소2';
        const place2Html = p2.length ? `
      <div class="place">
        <div class="place-title">${p2Name} (8틱)</div>
        <div class="ticks">${renderTicks(p2)}</div>
      </div>` : '';
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
      </li>`;
    }

    function renderResult(resp, root = document) {
        const resultSec = $('#result', root);
        const emptyP = $('#result-empty', root);
        const resultWrap = $('#result-wrap', root);
        const countSpan = $('#r-count', root);
        const comboListEl = $('#combo-list', root);

        resultSec.hidden = false;
        const combos = Array.isArray(resp?.combinations) ? resp.combinations : [];
        const count = Number.isInteger(resp?.count) ? resp.count : combos.length;

        if (combos.length === 0) {
            emptyP.hidden = false;
            resultWrap.hidden = true;
            return;
        }
        emptyP.hidden = true;
        resultWrap.hidden = false;
        countSpan.textContent = String(count);
        comboListEl.innerHTML = combos.map(renderCombo).join('');
    }

    // ===== 초기화 엔트리 =====
    function init({endpoint, formId, buttonId}) {
        const form = document.getElementById(formId);
        const btn = document.getElementById(buttonId);
        if (!form || !btn) return;

        btn.addEventListener('click', async () => {
            try {
                const payload = buildRequest(form);
                validateClient(payload);

                const headers = {'Content-Type': 'application/json'};

                btn.disabled = true;
                const oldText = btn.textContent;
                btn.textContent = '요청 중...';

                const res = await fetch(`${ctx}${endpoint}`, {
                    method: 'POST', headers, body: JSON.stringify(payload)
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

                document.dispatchEvent(new CustomEvent('sim:response', {detail: {ok: res.ok, data, endpoint}}));
                if (res.status === 429) {
                    const msg = (typeof data === 'object' && (data.message || data.detail)) || '짧은 시간 내에 동일한 요청을 보낼 수 없습니다.';
                    alert(msg);
                    return;
                }
                if (!res.ok || typeof data !== 'object') throw new Error(typeof data === 'string' ? data : '요청 실패');

                renderResult(data, document);
            } catch (err) {
                console.error('[Simulation ERROR]', err);
                alert(err?.message || '요청 중 오류가 발생했습니다.');
                document.dispatchEvent(new CustomEvent('sim:error', {detail: err}));
            } finally {
                btn.disabled = false;
                btn.textContent = '조합 조회';
            }
        });
    }

    window.SimulationUI = {init};
})();
