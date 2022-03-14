package cn.itrip.dao.itripUserLinkUser;
import cn.itrip.pojo.ItripUserLinkUser;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ItripUserLinkUserMapper {

	public List<ItripUserLinkUser>  select(@Param("id") Long id)throws Exception;
	public ItripUserLinkUser getItripUserLinkUserById(@Param(value = "id") Long id)throws Exception;

	public List<ItripUserLinkUser>	getItripUserLinkUserListByMap(Map<String, Object> param)throws Exception;

	public Integer getItripUserLinkUserCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertItripUserLinkUser(ItripUserLinkUser itripUserLinkUser)throws Exception;

	public int updateItripUserLinkUser(ItripUserLinkUser itripUserLinkUser)throws Exception;

	public Integer deleteItripUserLinkUserById(@Param(value = "id") Integer[] id)throws Exception;

}
