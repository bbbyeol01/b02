async function getList({bno, page, size, goLast}){
    const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}});

    if(goLast){
        const total = result.data.total;
        const lastPage = parseInt(Math.ceil(total / size));

        return getList({bno:bno, page:lastPage, size:size});
    }

    return result.data;

}// getList


async function addReply(replyObj) {
    console.log("addReply" + replyObj);
    const response = await axios.post(`/replies/`, replyObj);

    return response.data;
}// addReply