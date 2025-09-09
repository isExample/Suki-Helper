(() => {
    const $  = (sel, root=document) => root.querySelector(sel);
    const $$ = (sel, root=document) => Array.from(root.querySelectorAll(sel));

    const ctx = $('meta[name="ctx"]')?.content || '';

    function pickList(name){
        return $$(`input[name="${name}"]:checked`).map(el => el.value);
    }
    function buildConsumables(){
        const ids = ['DECAF_COFFEE','PUFFED_RICE','ROYAL_FEAST','CHOCOLATE_MILK','WHITE_MILK','COFFEE','COFFEE_X2'];
        const map = {};
        ids.forEach(k => {
            const el = $(`#qty-${k}`);
            const n = Number(el?.value || 0);
            if (n >= 1) map[k] = n;
        });
        return map;
    }
    function buildRequest(){
        return {
            targetStamina: Number($('#targetStamina')?.value || 0),
            fitnessLevel:  Number($('#fitnessLevel')?.value || 0),
            day: $('input[name="day"]:checked')?.value || 'WEEKDAY_OTHER',
            inactiveList: pickList('inactiveList'),
            activeList: pickList('activeList'),
            badgeList: pickList('badgeList'),
            traitList: pickList('traitList'),
            permanentItemList: pickList('permanentItemList'),
            consumableItemMap: buildConsumables()
        };
    }
    function validateClient(payload){
        // 커피 제약 검증
        const c1 = payload.consumableItemMap['COFFEE'] || 0;
        const c2 = payload.consumableItemMap['COFFEE_X2'] || 0;
        if (c1 > 1) throw new Error('커피는 1개를 초과할 수 없습니다.');
        if (c1 > 0 && c2 > 0) throw new Error('커피와 커피x2는 동시에 사용할 수 없습니다.');
    }

    // ===== Submit =====
    const btn = $('#buildRequestBtn');
    if (!btn) return;

    btn.addEventListener('click', async () => {
        try {
            const payload = buildRequest();
            validateClient(payload);

            const headers = { 'Content-Type': 'application/json' };

            btn.disabled = true;
            btn.textContent = '요청 중...';

            const res = await fetch(`${ctx}/api/v1/simulations/reach`, {
                method: 'POST',
                headers,
                body: JSON.stringify(payload),
            });

            const text = await res.text();
            const data = (() => { try { return JSON.parse(text); } catch { return text; } })();

            // 다른 스크립트/결과 UI에서 사용할 수 있도록 이벤트 발행
            document.dispatchEvent(new CustomEvent('reach:response', { detail: { ok: res.ok, data } }));

            if (!res.ok) throw new Error(typeof data === 'string' ? data : '요청 실패');

            // 임시: 콘솔 확인 (결과 UI 연동 전)
            console.log('[Reach OK]', data);
            alert('요청이 성공적으로 전송되었습니다.');

        } catch (err) {
            console.error('[Reach ERROR]', err);
            alert(err?.message || '요청 중 오류가 발생했습니다.');
            document.dispatchEvent(new CustomEvent('reach:error', { detail: err }));
        } finally {
            btn.disabled = false;
            btn.textContent = '조합 조회';
        }
    });
})();
