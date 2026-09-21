package com.yupi.springbootinit.model.dto.book;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Data
public class BookAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "书籍名不能为空")
    private String bookName;

    @NotBlank(message = "作者名不能为空")
    private String author;

    @Range(min = 30, max=50, message = "数量应该在{min}和{max}之间")
    private Integer totalNum;
}
