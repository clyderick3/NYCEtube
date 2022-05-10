import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { Categories } from 'app/shared/model/enumerations/categories.model';

export interface IVideos {
  id?: number;
  name?: string;
  url?: string | null;
  date?: string | null;
  videoContentType?: string;
  video?: string;
  categories?: Categories | null;
  thumbnailContentType?: string;
  thumbnail?: string;
  user?: IUser | null;
}

export const defaultValue: Readonly<IVideos> = {};
