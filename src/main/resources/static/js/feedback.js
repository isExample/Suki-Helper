function validateFeedbackMessage(text) {
    const msg = (text || '').trim();
    if (msg.length < 10)  throw new Error('메시지는 최소 10자 이상이어야 합니다.');
    if (msg.length > 200) throw new Error('메시지는 최대 200자 이하로 작성해주세요.');
}

(function initFeedback(){
    const $  = (sel, root=document) => root.querySelector(sel);

    const btn    = $('#feedbackBtn');
    const panel  = $('#feedbackPanel');
    const form   = $('#feedbackForm');
    if (!btn || !panel || !form) return;

    const closeBtn = panel.querySelector('.fb-close');
    const cancel   = panel.querySelector('.fb-cancel');
    const submit   = panel.querySelector('.fb-submit');

    // 컨텍스트/CSRF 메타
    const ctx        = $('meta[name="ctx"]')?.content || '';
    const csrfToken  = $('meta[name="_csrf"]')?.content;
    const csrfHeader = $('meta[name="_csrf_header"]')?.content;

    function openPanel(){
        panel.classList.add('is-open');
        panel.setAttribute('aria-hidden', 'false');
        $('#fbType')?.focus();
    }
    function closePanel(){
        panel.classList.remove('is-open');
        panel.setAttribute('aria-hidden', 'true');
    }

    // 토글
    btn.addEventListener('click', () => {
        const isOpen = panel.classList.contains('is-open');
        isOpen ? closePanel() : openPanel();
    });
    closeBtn?.addEventListener('click', closePanel);
    cancel?.addEventListener('click', closePanel);

    // ESC로 닫기
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && panel.classList.contains('is-open')) closePanel();
    });

    // 제출
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const endpoint = '/api/v1/support';

        const payload = {
            type:    $('#fbType').selectedOptions?.[0]?.text?.trim() || '기타',
            message: ($('#fbMessage')?.value || '').trim()
        };
        if (!payload.message) { alert('메시지를 입력하세요.'); return; }

        const headers = { 'Content-Type': 'application/json' };
        if (csrfToken && csrfHeader) headers[csrfHeader] = csrfToken;

        submit.disabled = true;
        const old = submit.textContent; submit.textContent = '전송 중...';
        try{
            const res  = await fetch(`${ctx}${endpoint}`, { method:'POST', headers, body: JSON.stringify(payload) });
            const text = await res.text();
            let data; try { data = JSON.parse(text); } catch { data = text; }

            validateFeedbackMessage(payload.message);

            if (res.status === 429) {
                const msg = (typeof data === 'object' && (data.message || data.detail))
                    || '짧은 시간 내에 동일한 요청을 보낼 수 없습니다.';
                alert(msg);
                return;
            }
            if (!res.ok) throw new Error(typeof data === 'string' ? data : (data?.message || '제출 실패'));

            alert('피드백이 전송되었습니다. 감사합니다!');
            form.reset();
            closePanel();
        } catch(err){
            console.error('[Feedback ERROR]', err);
            alert(err?.message || '제출 중 오류가 발생했습니다.');
        } finally {
            submit.disabled = false;
            submit.textContent = old;
        }
    });
})();
