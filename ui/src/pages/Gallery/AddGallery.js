import React, { useState } from 'react'

import { addGallery, addImages } from 'api/job/job'

export default function AddGallery() {
  const [image, setimage] = useState('')

  const onFileChangeHandler = async (e) => {
    e.preventDefault()
    const formData = new FormData()
    formData.append('file', e.target.files[0])

    const response = await addImages(formData)
    //const res = await getAllGallery()
    //console.log(res[0].base64)
    setimage(response.data)
    console.log(response)
    const gallery = {
      fileEntity: response,
      jobId: 'caa71518-c3a0-41e3-ba5c-efa412755f87',
    }
    const res = await addGallery(gallery)

    console.log(res)
  }

  return (
    <div className='container'>
      <div className='row'>
        <div className='col-md-6'>
          <div className='form-group files color'>
            <label>Upload Your File </label>
            <input
              type='file'
              className='form-control'
              name='file'
              onChange={onFileChangeHandler}
            />
            <img src={`data:image/jpeg;base64,${image}`} />
          </div>
        </div>
      </div>
    </div>
  )
}
