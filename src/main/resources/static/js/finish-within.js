(() => {
    if (!window.SimulationUI) return;
    window.SimulationUI.init({
        endpoint: '/api/v1/simulations/finish-within',
        formId: 'finishWithinForm',
        buttonId: 'finishWithinBtn'
    });
})();
