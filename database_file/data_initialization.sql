-- table categories --
INSERT INTO `hanSoloDB`.`categories` (`gmt_create`, `gmt_modify`, `name`, `status`, `type`) VALUES
(now(), now(), 'Gọng Nam', 1, 'CATEGORY'),
(now(), now(), 'Gọng Nữ', 1, 'CATEGORY'),
(now(), now(), 'Spring Collection', 1, 'COLLECTION');

-- table member --
INSERT INTO `hanSoloDB`.`members` (`address`, `email`, `fullname`, `gmt_create`, `gmt_modify`, `pass`, `phone`, `status`) VALUES
('91/5 trần chánh chiếu, phường 14, quận5, tp Hồ Chí Minh', 'thienkyo@gmail.com', 'Thien Le', now(), now(), 'MTIzNDU2bHR0', '0909957872', 1);


-- table member_role --
INSERT INTO `hanSoloDB`.`member_role` (`gmt_create`, `gmt_modify`, `level`, `role`, `member_id`) VALUES
(now(), now(), '0', 'ADMIN', 1);

-- table banners --
INSERT INTO `hanSoloDB`.`banners` (`description`, `gmt_create`, `gmt_modify`, `image`, `link`, `name`, `status`, `type`, `need_text`) VALUES
('A specialist label creating luxury essentials. Ethically crafted with an unwavering commitment to exceptional quality.', now(), now(), 'hero-9.jpg', 'f/sdfe/w234', 'Fall - Winter Collections 2030', 1, 'HOMEBANNER',1),
('A specialist label creating luxury essentials. Ethically crafted with an unwavering commitment to exceptional quality.', now(), now(), 'hero-7.jpeg', 'f/sdfe/w234', 'Fall - Winter Collections 2030', 1, 'HOMEBANNER',1),
('A specialist label creating luxury essentials. Ethically crafted with an unwavering commitment to exceptional quality.', now(), now(), 'hero-5.jpg', 'f/sdfe/w234', 'Fall - Winter Collections 2055', 1, 'HOMEBANNER',1),
('', now(), now(), 'banner-4.jpeg', 'f/sdfe/w234', 'Fall - Winter Collections 2055', 1, 'HOMECOLLECTION',1),
('', now(), now(), 'banner-2.jpeg', 'f/sdfe/w234', 'Fall - Winter Collections 2055', 1, 'HOMECOLLECTION',1),
('', now(), now(), 'banner-6.jpeg', 'f/sdfe/w234', 'Fall - Winter Collections 2055', 1, 'HOMECOLLECTION',1);

-- table banners --
INSERT INTO `hanSoloDB`.`suppliers` (`address`, `gmt_create`, `gmt_modify`, `logo`, `name`, `phone`, `prefix`) VALUES
('345 Trần Chánh Chiếu, p14,q5, hcm', now(), now(), 'logo.jpg', 'Công ty 101', '0909 000 000 0', 'ON');


-- table banners --
INSERT INTO `hanSoloDB`.`products` (`buy_price`, `description`, `discount`, `gmt_create`, `gmt_modify`, `images`, `merchant_product_id`, `name`, `quantity`, `sell_price`, `status`, `suppliers_id`) VALUES
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 0, now(), now(), 'product-22.jpg', 'VL26887', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 0, now(), now(), 'product-23.jpg', 'VL26880', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 0, now(), now(), 'product-24.jpg', 'VL26881', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 0, now(), now(), 'product-25.jpg', 'VL268872', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 0, now(), now(), 'product-18.jpg', 'VL26884', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 0, now(), now(), 'product-19.jpg', 'VL26882', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 0, now(), now(), 'product-20.jpg', 'VL26885', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1),
('50000', '- Gọng kính được làm bằng hợp kim cao cấp thanh mảnh, nhẹ và rất bền. \n\n- Càng kính cũng làm bằng hợp kim mảnh , bền và không phai màu, chân kính nhẹ có bọc nhựa giúp người mang kính trong thời gian dài cũng không đau ở vành tai.', 0, now(), now(), 'product-21.jpg', 'VL268870', 'Velocity Eyewear - Glasses - VL26887', '1', '370000', 1, 1);
