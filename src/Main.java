void main() {
    Database db = Database.getInstance();
    menuHandler menu = new menuHandler();
    db.connect();
    menu.displayMenu();
    db.disconnect();
}
