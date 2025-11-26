void main() {
    database db = database.getInstance();
    menuHandler menu = new menuHandler();
    db.connect();
    menu.displayMenu();
    db.disconnect();
}
