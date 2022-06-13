package com.ldnhat.springbootblog.service.impl;

import com.ldnhat.springbootblog.dto.PostDto;
import com.ldnhat.springbootblog.entity.PostEntity;
import com.ldnhat.springbootblog.exception.ResourceNotFoundException;
import com.ldnhat.springbootblog.payload.PostResponse;
import com.ldnhat.springbootblog.repository.PostRepository;
import com.ldnhat.springbootblog.service.PostService;
import net.bytebuddy.pool.TypePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public PostDto savePost(PostDto postDto) {
        PostEntity postEntity = mapToEntity(postDto);

        PostEntity newPost = postRepository.save(postEntity);

        //convert entity to dto
        return mapToDto(newPost);
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<PostEntity> pagePost = postRepository.findAll(pageable);
        List<PostEntity> listOfPosts = pagePost.getContent();

        List<PostDto> content = listOfPosts.stream().map(this::mapToDto).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();

        postResponse.setContent(content);
        postResponse.setPageNo(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setLast(pagePost.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post", "id", id));
        return mapToDto(postEntity);
    }

    @Override
    public PostDto editPost(PostDto postDto, Long id) {
        // get post by id from the database
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post", "id", id));
        postEntity.setTitle(postDto.getTitle());
        postEntity.setContent(postDto.getContent());
        postEntity.setDescription(postDto.getDescription());

        PostEntity updatedPost = postRepository.save(postEntity);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePostById(Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post", "id", id));
        postRepository.delete(postEntity);
    }

    private PostDto mapToDto(PostEntity postEntity){
        PostDto postDto = new PostDto();
        postDto.setId(postEntity.getId());
        postDto.setTitle(postEntity.getTitle());
        postDto.setContent(postEntity.getContent());
        postDto.setDescription(postEntity.getDescription());

        return postDto;
    }

    private PostEntity mapToEntity(PostDto postDto){
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(postDto.getTitle());
        postEntity.setContent(postDto.getContent());
        postEntity.setDescription(postDto.getDescription());

        return postEntity;
    }
}
