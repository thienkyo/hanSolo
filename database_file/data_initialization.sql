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
('A specialist label creating luxury essentials. Ethically crafted with an unwavering commitment to exceptional quality.', now(), now(), 'hero-9.jpg', 'f/sdfe/w234', 'Fall - Winter Collections 2030', 1, 'TYPE1',1),
('A specialist label creating luxury essentials. Ethically crafted with an unwavering commitment to exceptional quality.', now(), now(), 'hero-7.jpg', 'f/sdfe/w234', 'Fall - Winter Collections 2030', 1, 'TYPE1',1),
('A specialist label creating luxury essentials. Ethically crafted with an unwavering commitment to exceptional quality.', now(), now(), 'hero-5.jpg', 'f/sdfe/w234', 'Fall - Winter Collections 2055', 1, 'TYPE1',1);
