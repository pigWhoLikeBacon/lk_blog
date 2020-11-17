/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.*;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.Collection;

/**
* @website https://el-admin.vip
* @description /
* @author LK
* @date 2020-11-16
**/
@Entity
@Data
@Table(name="lkblog_article")
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    @ApiModelProperty(value = "ID")
    private Long articleId;

    @ManyToMany
    @JoinTable(name = "lkblog_article_tag",
            joinColumns = {@JoinColumn(name = "article_id",referencedColumnName = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id",referencedColumnName = "tag_id")})
    @ApiModelProperty(value = "标签")
    private Collection<Tag> Tags;

    @Column(name = "cover",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "文章封面")
    private String cover;

    @Column(name = "introduce",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "简介")
    private String introduce;

    @Column(name = "title",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "题目")
    private String title;

    @Column(name = "content",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "内容")
    private String content;

    @Column(name = "views")
    @ApiModelProperty(value = "浏览量")
    private Long views;

    @Column(name = "is_show",nullable = false)
    @NotNull
    @ApiModelProperty(value = "是否展示")
    private Boolean isShow;

    @Column(name = "create_time")
    @CreationTimestamp
    @ApiModelProperty(value = "创建日期")
    private Timestamp createTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    public void copy(Article source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}