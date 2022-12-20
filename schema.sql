create table if not exists ads
(
    id      varchar primary key,
    name    varchar,
    ad_text varchar
);

create table impression_events
(
    ad_id     varchar REFERENCES ads (id) on delete cascade,
    timestamp timestamp
);

create table click_events
(
    ad_id     varchar REFERENCES ads (id) on delete cascade,
    timestamp timestamp,
    foreign key (ad_id) REFERENCES ads (id)
);

create view ctp
as
select ad.id,
       (case
            when
                ((SELECT COUNT(*) FROM impression_events as e where e.ad_id = ad.id) > 0)
                then
                cast((SELECT COUNT(*) FROM click_events as e where e.ad_id = ad.id)::float /
                     (SELECT COUNT(*) FROM impression_events as e where e.ad_id = ad.id) as float)
            else 0
           end) as ctp
from ads as ad;