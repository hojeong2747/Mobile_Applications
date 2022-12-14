package ddwu.mobile.lecture.etc.myretrofittest.model;

public class DailyBoxOfficeList {

    private String scrnCnt;

    private String rankInten;

    private String audiCnt;

    private String movieCd;

    private String salesShare;

    private String audiAcc;

    private String audiInten;

    private String salesInten;

    private String salesAcc;

    private String rank;

    private String movieNm;

    private String rankOldAndNew;

    private String showCnt;

    private String rnum;

    private String openDt;

    private String salesChange;

    private String salesAmt;

    private String audiChange;

    public String getScrnCnt ()
    {
        return scrnCnt;
    }

    public void setScrnCnt (String scrnCnt)
    {
        this.scrnCnt = scrnCnt;
    }

    public String getRankInten ()
    {
        return rankInten;
    }

    public void setRankInten (String rankInten)
    {
        this.rankInten = rankInten;
    }

    public String getAudiCnt ()
    {
        return audiCnt;
    }

    public void setAudiCnt (String audiCnt)
    {
        this.audiCnt = audiCnt;
    }

    public String getMovieCd ()
    {
        return movieCd;
    }

    public void setMovieCd (String movieCd)
    {
        this.movieCd = movieCd;
    }

    public String getSalesShare ()
    {
        return salesShare;
    }

    public void setSalesShare (String salesShare)
    {
        this.salesShare = salesShare;
    }

    public String getAudiAcc ()
    {
        return audiAcc;
    }

    public void setAudiAcc (String audiAcc)
    {
        this.audiAcc = audiAcc;
    }

    public String getAudiInten ()
    {
        return audiInten;
    }

    public void setAudiInten (String audiInten)
    {
        this.audiInten = audiInten;
    }

    public String getSalesInten ()
    {
        return salesInten;
    }

    public void setSalesInten (String salesInten)
    {
        this.salesInten = salesInten;
    }

    public String getSalesAcc ()
    {
        return salesAcc;
    }

    public void setSalesAcc (String salesAcc)
    {
        this.salesAcc = salesAcc;
    }

    public String getRank ()
    {
        return rank;
    }

    public void setRank (String rank)
    {
        this.rank = rank;
    }

    public String getMovieNm ()
    {
        return movieNm;
    }

    public void setMovieNm (String movieNm)
    {
        this.movieNm = movieNm;
    }

    public String getRankOldAndNew ()
    {
        return rankOldAndNew;
    }

    public void setRankOldAndNew (String rankOldAndNew)
    {
        this.rankOldAndNew = rankOldAndNew;
    }

    public String getShowCnt ()
    {
        return showCnt;
    }

    public void setShowCnt (String showCnt)
    {
        this.showCnt = showCnt;
    }

    public String getRnum ()
    {
        return rnum;
    }

    public void setRnum (String rnum)
    {
        this.rnum = rnum;
    }

    public String getOpenDt ()
    {
        return openDt;
    }

    public void setOpenDt (String openDt)
    {
        this.openDt = openDt;
    }

    public String getSalesChange ()
    {
        return salesChange;
    }

    public void setSalesChange (String salesChange)
    {
        this.salesChange = salesChange;
    }

    public String getSalesAmt ()
    {
        return salesAmt;
    }

    public void setSalesAmt (String salesAmt)
    {
        this.salesAmt = salesAmt;
    }

    public String getAudiChange ()
    {
        return audiChange;
    }

    public void setAudiChange (String audiChange)
    {
        this.audiChange = audiChange;
    }

    @Override
    public String toString()
    {
        return rank +". "+movieNm+" [" +openDt+"]";
    }

}
