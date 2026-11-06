function Progress_bar({bgcolor,progress,height}){

    const Parentdiv = {
        height: height,
        width: '100%',
        backgroundColor: 'whitesmoke',
        borderRadius: 40,
        margin: 50
    }

    const Childdiv = {
        height: '100%',
        width: `${progress}%`,
        backgroundColor: bgcolor,
        borderRadius:40,

    }

    const progresstext = {
        padding: 10,
        color: 'black',
        fontWeight: 900
    }

    return (
        <div style={Parentdiv} className={"max-w-sm md:max-w-md shadow-md"}>
            <div style={Childdiv} className={"text-right"}>
                <span style={progresstext}>{`${progress}%`}</span>
            </div>
        </div>
    )
}

export default Progress_bar;