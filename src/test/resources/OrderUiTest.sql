create table if not exists t_user (
    id uuid DEFAULT gen_random_uuid() primary key,
    userName varchar(100) UNIQUE,
    emailAddress varchar(100) UNIQUE,
    address varchar(100),
    phone varchar(100),
    password varchar(100),
    enabled BOOLEAN,
    secret varchar(100),
    mfaEnabled BOOLEAN
);


INSERT INTO t_product
(name, price, stock) VALUES
('ASICS マジックスピード4', 18700, 10)
,('NIKE　エアジョーダン1', 280000, 20)
,('Arcteryx　Alpha SV Jacket', 150000, 30)
,('Patagonia フリース', 20000, 40)
,('モンベル　ライトダウン', 12000, 50)
,('アディダス　スーパースター', 15800, 60)
,('コンバース　ジャックパーセル', 16800, 70)
;


INSERT INTO employee
(employeename,phone,emailaddress) VALUES
('佐藤','090-1234-5678','asato@ims.co.jp')
,('高橋','090-9754-9637','btakahashi@ims.co.jp')
,('鈴木','090-9274-1234','csuzuki@ims.co.jp')
,('渡辺','090-8932-0987','dwatanabe@ims.co.jp')
,('遠藤','090-8976-0164','eendo@ims.co.jp')
;
