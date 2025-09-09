(() => {
    if (!window.SimulationUI) return;
    window.SimulationUI.init({
        endpoint: '/api/v1/simulations/finish-at',
        formId: 'finishAtForm',
        buttonId: 'finishAtBtn'
    });
})();
