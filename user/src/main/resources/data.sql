INSERT INTO contact_info(id, first_name, last_name, email, number)
VALUES ('b8181463-a15f-4eda-9d3b-e0e7ce2559a6', 'Davor', 'Sekulic', 'davorsekulic2@gmail.com', '062087644');


INSERT INTO users(id, city, date_created, location, password, role, username, contact_info_id)
VALUES ('b8181463-a15f-4eda-9d3b-e0e7ce2559b6','Sarajevo','21.11.1998',null,'user1234','ROLE_ADMIN','dale','b8181463-a15f-4eda-9d3b-e0e7ce2559a6'),

select *
from contact_info;

select * from users