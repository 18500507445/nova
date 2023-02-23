package com.nova.ebook.effectivejava.chapter5.section8;

import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/23 20:04
 */
@Service
interface BaseService<M extends BaseMapper<E>, E extends Base> {

}
