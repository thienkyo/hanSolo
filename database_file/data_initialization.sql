-- table categories --
INSERT INTO `hanSoloDB`.`categories` (`gmt_create`, `gmt_modify`, `name`, `status`, `type`) VALUES
(now(), now(), 'Gọng Nam', 1, 'CATEGORY'),
(now(), now(), 'Gọng Nữ', 1, 'CATEGORY'),
(now(), now(), 'Spring Collection', 1, 'COLLECTION');

-- table member --
INSERT INTO `hanSoloDB`.`members` (`address`, `email`, `fullname`, `gmt_create`, `gmt_modify`, `pass`, `phone`, `status`) VALUES
('91/5 trần chánh chiếu, phường 14, quận5, tp Hồ Chí Minh', 'thienkyo@gmail.com', 'Thien Le', now(), now(), 'MTIzNDU2bHR0', '0909957872', 1);


-- table member_role --
INSERT INTO `hanSoloDB`.`member_role` (`gmt_create`, `gmt_modify`, `level`, `role`, `member_id`, `name`, `phone`) VALUES
(now(), now(), '0', 'ADMIN', 1,'Thien Le','0909957872'),
(now(), now(), '0', 'MEMBER', 1,'Thien Le','0909957872');

-- table banners --
INSERT INTO `hanSoloDB`.`banners` (`description`, `gmt_create`, `gmt_modify`, `image`, `link`, `name`, `status`, `type`, `need_text`) VALUES
('A specialist label creating luxury essentials. Ethically crafted with an unwavering commitment to exceptional quality.', now(), now(), 'hero-9.jpg', 'f/sdfe/w234', 'Ten ten Collection 222', 1, 'HOMEBANNER',1),
('A specialist label creating luxury essentials. Ethically crafted with an unwavering commitment to exceptional quality.', now(), now(), 'hero-7.jpeg', 'f/sdfe/w234', 'Kinh Nguyen special 3000', 0, 'HOMEBANNER',1),
('A specialist label creating luxury essentials. Ethically crafted with an unwavering commitment to exceptional quality.', now(), now(), 'hero-5.jpg', 'f/sdfe/w234', 'Fall - Winter Collections 2055', 1, 'HOMEBANNER',1),
('', now(), now(), 'banner-4.jpeg', 'f/sdfe/w234', 'Youth Collections 2055', 1, 'HOMECOLLECTION',1),
('', now(), now(), 'banner-2.jpg', 'f/sdfe/w234', 'Fall Special', 1, 'HOMECOLLECTION',1),
('', now(), now(), 'banner-6.jpg', 'f/sdfe/w234', 'Kinh Nguyen Choices', 1, 'HOMECOLLECTION',1);

-- table banners --
INSERT INTO `hanSoloDB`.`suppliers` (`address`, `gmt_create`, `gmt_modify`, `logo`, `name`, `phone`, `prefix`) VALUES
('345 Trần Chánh Chiếu, p14,q5, hcm', now(), now(), 'logo.jpg', 'Công ty 101', '0909 000 000 0', 'ON');


-- table products --
INSERT INTO `hanSoloDB`.`products` (`buy_price`, `description`, `discount`, `gmt_create`, `gmt_modify`, `images`, `merchant_product_id`, `name`, `quantity`, `sell_price`, `status`, `suppliers_id`) VALUES
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 0, now(), now(), 'product-22.jpg', 'VL26887', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 0, now(), now(), 'product-23.jpg', 'VL26880', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 0, now(), now(), 'product-24.jpg', 'VL26881', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 0, now(), now(), 'product-25.jpg', 'VL268872', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 0, now(), now(), 'product-18.jpg', 'VL26884', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 0, now(), now(), 'product-19.jpg', 'VL26882', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 0, now(), now(), 'product-20.jpeg', 'VL26885', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 0, now(), now(), 'product-21.jpeg', 'VL268870', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 15, now(), now(), 'product-21.jpeg', 'VL26887011', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 20, now(), now(), 'product-21.jpeg', 'VL26887065', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 20, now(), now(), 'product-21.jpeg', 'VL26887000', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 25, now(), now(), 'product-21.jpeg', 'VL26887034', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1);

-- table products --
INSERT INTO `hanSoloDB`.`products_categories` (`product_id`, `category_id`) VALUES
( 1, 1),( 2, 1),( 3, 1),( 4, 1),
( 5, 1),( 6, 1),( 7, 2),( 8, 2),
( 9, 2),( 10, 2),( 11, 2),( 12, 2),
( 5, 3),( 6, 3),( 7, 3);

-- table articles --
INSERT INTO `hanSoloDB`.`articles` (`author`, `content`, `description`, `gmt_create`, `gmt_modify`, `name`, `status`, `thumbnail`) VALUES
('kyo', '<p>nothing much to say here.&nbsp;nothing much to say here.&nbsp;nothing much to say here.nothing much to say here.&nbsp;nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.&nbsp;nothing much to say here.nothing much to say here.nothing much to say here.nothing <span style=\"color: #ff6600;\">much to say here</span>.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.</p>\\n<p>&nbsp;</p>\\n<p style=\"text-align: center;\"><img src=\"images/article/20170510.211333-girl2.jpg\" alt=\"\" width=\"484\" height=\"441\" /></p>\\n<h1>nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.</h1>\\n<p>&nbsp;</p>\\n<p>nothing much to say here.n<span style=\"color: #339966;\">othing much</span> to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.nothing much to say here.</p>', 'nothing much to say here.nothing much to say here.nothing much to say here. thiện thiện tài giỏi', now(), now(), 'bài viết article đầu tiên của tôi 343', 1, 'blog-1.jpg'),
('kyo', 'khống co gì nhiều để nói.khống co gì nhiều để nói.khống co gì nhiều để nói.khống co gì nhiều để nói.khống co gì nhiều để nói.khống co gì nhiều để nói.\'', 'khống co gì nhiều để nói.khống co gì nhiều để nói.khống co gì nhiều để nói.khống co gì nhiều để nói.khống co gì nhiều để nói.', now(), now(), 'đây là bài thứ 2 tôi viết lan dau a 44', 1, 'blog-2.jpg'),
('kyo', '<p>bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3</p>\\n<h1>bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3bai thu 3 baif thu3</h1>\\n<p>&nbsp;</p>\\n<p style=\"text-align: center;\"><img src=\"images/article/20170510.231336-man-four.jpg\" alt=\"\" width=\"121\" height=\"86\" /></p>\\n<p style=\"text-align: center;\"><img src=\"images/article/20170510.231404-blog-two.jpg\" alt=\"\" width=\"866\" height=\"396\" /></p>\\n<p style=\"text-align: center;\"><img src=\"images/article/20170512.222115-girl2.jpg\" alt=\"\" width=\"484\" height=\"441\" /></p>\\n<p style=\"text-align: center;\">&nbsp;</p>', 'tes tes tes test', now(), now(), 'bai thu 3 baif thu3', 1, 'blog-3.jpg');


