function checking() {
    if (document.report.type.value === '') {
        alert('必須入力項目があります');
    } else {
        save_cookie();
    }
}