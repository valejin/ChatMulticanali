package it.uniroma2.dicii.bd.model.domain;

public class Credentials {

        private final String cf;
        private final String password;
        private final Role role;
        private final String username;

        public Credentials(String cf, String password, Role role, String username) {
            this.cf = cf;
            this.password = password;
            this.role = role;
            this.username = username;
        }

        public String getCF() {
            return cf;
        }

        public String getPassword() {
            return password;
        }

        public Role getRole() {
            return role;
        }

        public String getUsername() {
            return username;
        }

}
