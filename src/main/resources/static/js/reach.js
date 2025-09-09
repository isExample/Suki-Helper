(() => {
    if (!window.SimulationUI) return;
    window.SimulationUI.init({
        endpoint: '/api/v1/simulations/reach',
        formId: 'reachForm',
        buttonId: 'reachBtn'
    });
})();
